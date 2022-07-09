package Modules.Calculator.Links;

import Modules.Calculator.IllegalMathsException;

import java.util.HashMap;

public class LinkItem {
	
	private LinkItem item, itemA, itemB;
	private LinkType link;
	private String variable;
	private Double value;
	
	private boolean negative;
	
	public double evaluate(HashMap<String, SpaceVariable> variableSpace) throws IllegalMathsException {
		if(value != null) {
			return !negative ? value : -value;
		} else if(variable != null) {
			return !negative ? variableSpace.get(variable).value : -variableSpace.get(variable).value;
		} else if(link instanceof JoiningToken) {
			if(link == JoiningToken.ADD)      return itemA.evaluate(variableSpace) + itemB.evaluate(variableSpace);
			else if(link == JoiningToken.SUB) return itemA.evaluate(variableSpace) - itemB.evaluate(variableSpace);
			else if(link == JoiningToken.MUL) return itemA.evaluate(variableSpace) * itemB.evaluate(variableSpace);
			else if(link == JoiningToken.DIV) return itemA.evaluate(variableSpace) / itemB.evaluate(variableSpace);
			else if(link == JoiningToken.POW) return Math.pow(itemA.evaluate(variableSpace), itemB.evaluate(variableSpace));
		} else if(link instanceof PrefixToken) {
			if(link == PrefixToken.NEG) return -item.evaluate(variableSpace);
		} else if(link instanceof SuffixToken) {
			if(link == SuffixToken.DEG) return Math.toRadians(item.evaluate(variableSpace));
		}
		throw new IllegalMathsException("LinkItem.evaluate() IN THIS CASE NOT DEFINED");
	}
	
	public static LinkItem generateTerm(LinkItem itemA, JoiningToken joiner, LinkItem itemB) {
		LinkItem out = new LinkItem();
		out.itemA = itemA;
		out.itemB = itemB;
		out.link = joiner;
		return out;
	}
	
	public static LinkItem generateFunction(LinkItem item, SuffixToken suffixToken) {
		LinkItem out = new LinkItem();
		out.item = item;
		out.link = suffixToken;
		return out;
	}
	
	public static LinkItem generateFunction(LinkItem item, PrefixToken prefixToken) {
		LinkItem out = new LinkItem();
		out.item = item;
		out.link = prefixToken;
		return out;
	}
	
	public static LinkItem generateItem(double value) {
		LinkItem out = new LinkItem();
		out.value = value;
		return out;
	}
	
	public static LinkItem generateItem(String variableName) {
		LinkItem out = new LinkItem();
		out.variable = variableName;
		return out;
	}
	
	private LinkItem() {}
	
}
