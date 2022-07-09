package Modules.Calculator.Links;

import Modules.Calculator.IllegalMathsException;

public class SpaceVariable {
	
	public final String name;
	public double value;
	public final boolean constant;
	
	public SpaceVariable(String name) {
		this.name = name;
		this.value = 0;
		this.constant = false;
	}
	
	public void setValue(double value) throws IllegalMathsException {
		if(!constant) {
			this.value = value;
		} else {
			throw new IllegalMathsException("Constants cannot be changed.");
		}
	}
	
	private SpaceVariable(String name, double value) {
		this.name = name;
		this.value = value;
		this.constant = true;
	}
}
