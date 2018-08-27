package com.flasm;

public enum FlasmConversion {
	
	AS("AS"),
	PCODE("PCODE");
	
	private String conversion;
	
	public String getConversion() {
		return (this.conversion);
	}
	
	FlasmConversion(String conversion) {
		this.conversion = conversion;
	}
}
