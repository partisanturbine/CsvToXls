package com.digsolab;

public class Format {
     
    private Type type;
    private String mask;
    
    public Format(Type type, String mask) {
    	this.type = type;
    	this.mask = mask;
    }
    
    public Type getType() {
    	return this.type;
    }
    
    public String getMask() {
    	return this.mask;
    }
}
