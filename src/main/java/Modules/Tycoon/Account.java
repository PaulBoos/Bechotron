package Modules.Tycoon;

import Utils.Numbers;

import java.sql.SQLException;

public class Account {
	
	private final TycoonDatabase database;
	private final Currency currency;
	private final long BAN;
	private Integer cachedBalance = null;
	protected boolean fromDatabase;
	
	public Account(TycoonDatabase database, Currency currency, long BAN, boolean fromDatabase) {
		this.database = database;
		this.currency = currency;
		this.BAN = BAN;
		this.fromDatabase = fromDatabase;
	}
	
	/**
	 * Make a request to change an account.
	 * @param amount to add/remove from the account.
	 * @return the created request.
	 */
	public ChangeRequest makeRequest(int amount) {
		return new ChangeRequest(this, amount);
	}
	
	public int getBalance() {
		if(cachedBalance == null) {
			try {
				cachedBalance = database.getBalance(BAN, currency.ordinal());
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return cachedBalance;
	}
	private void setBalance(int newBalance) {
		cachedBalance = newBalance;
		try {
			database.saveBalance(this);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Currency getCurrency() {
		return currency;
	}
	public long getBAN() {
		return BAN;
	}
	
	public static class ChangeRequest {
		
		private final Account account;
		private final int amount;
		
		private int newBalance() {
			return account.getBalance() + amount;
		}
		
		/**
		 * Check if the transaction is fulfillable.
		 * @return true if the transaction is fulfillable without going into debt, or deleverages debt.
		 * @throws Numbers.IntegerOverflowException if fulfillment would result in an integer overflow.
		 */
		public boolean isFulfillable() throws Numbers.IntegerOverflowException {
			if(!Numbers.sumIsOverflowSafe(account.getBalance(), amount)) throw new Numbers.IntegerOverflowException();
			return ((amount > 0) || (account.getBalance() + amount > 0)); // Check if value is added OR if the withdrawal is affordable.
		}
		
		/**
		 * Fulfill the transaction if possible.
		 * @return true if the transaction was fulfillable without going into debt, or deleverages debt.
		 * @throws Numbers.IntegerOverflowException if fulfillment would result in an integer overflow.
		 */
		public boolean attemptFulfillment() throws Numbers.IntegerOverflowException {
			if(isFulfillable()) {
				account.setBalance(newBalance());
				return true;
			} else return false;
		}
		
		/**
		 * Force the fulfillment of the transaction, even if the account goes into credit.
		 * @throws Numbers.IntegerOverflowException if fulfillment would result in an integer overflow.
		 */
		public void forceFulfillment() throws Numbers.IntegerOverflowException {
			if(!Numbers.sumIsOverflowSafe(account.getBalance(), amount)) throw new Numbers.IntegerOverflowException();
			account.setBalance(newBalance());
		}
		
		private ChangeRequest(Account account, int amount) {
			this.account = account;
			this.amount = amount;
		}
		
	}
}
