package Modules.Calculator;

import Modules.Calculator.Links.*;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {
	
	LinkType[] orderOfOperations = new LinkType[] {
			Value.GENERIC_VALUE_TYPE,
			Variable.GENERIC_VARIABLE_TYPE,
			PrefixToken.NEG,
			SuffixToken.DEG,
			JoiningToken.POW,
			JoiningToken.DIV,
			JoiningToken.MUL,
			JoiningToken.SUB,
			JoiningToken.ADD
	};
	
	public static void main(String[] args) throws IllegalMathsException {
		parse("1+2-3*4/5^6()");
	}
	
	/**
	 * Method for parsing a mathematical term.
	 * Generates a tree of LinkItems.
	 *
	 * @param termToParse The String that is to be parsed.
	 * @return the root LinkItem generated.
	 */
	public static LinkItem parse(String termToParse) throws IllegalMathsException {
		List<Object> tokenList = new ArrayList<>();
		{
			StringBuilder buffer = new StringBuilder();
			int cursor = 0;
			while(cursor < termToParse.length()) {
				char newChar = termToParse.charAt(cursor);
				switch(newChar) {
					case '+' -> {
						pushBuffer(buffer, tokenList);
						tokenList.add(JoiningToken.ADD);
					}
					case '*' -> {
						pushBuffer(buffer, tokenList);
						tokenList.add(JoiningToken.MUL);
					}
					case '/' -> {
						pushBuffer(buffer, tokenList);
						tokenList.add(JoiningToken.DIV);
					}
					case '-' -> {
						pushBuffer(buffer, tokenList);
						tokenList.add(JoiningToken.SUB);
					}
					case '^' -> {
						pushBuffer(buffer, tokenList);
						tokenList.add(JoiningToken.POW);
					}
					case '(' -> {
						pushBuffer(buffer, tokenList);
						tokenList.add(Parentheses.OPEN);
					}
					case ')' -> {
						pushBuffer(buffer, tokenList);
						tokenList.add(Parentheses.CLOSED);
					}
					default -> buffer.append(newChar);
				}
			}
		}
		
		LinkItem rootItem = null;
		
		
		
		return rootItem;
	}
	
	private static LinkItem pushBuffer(StringBuilder buffer, List<Object> tokenList) throws IllegalMathsException {
		LinkItem output = null;
		if(!buffer.isEmpty()) {
			if(buffer.toString().matches("\\d{1,8}(\\.\\d{1,8})?")) { // Filter out numbers
				double value = Double.parseDouble(buffer.toString());
				output = LinkItem.generateItem(value);
				tokenList.add(output);
				buffer.delete(0,buffer.length());
			} else if(buffer.toString().matches("[a-z]?\\w{0,15}")) { // Filter out variables
				output = LinkItem.generateItem(buffer.toString());
				tokenList.add(output);
				buffer.delete(0,buffer.length());
			} else {
				throw new IllegalMathsException(
						"The string " + buffer + " is not compliant with the rules on defining variables/constants.");
			}
		}
		return output;
	}
	
}
