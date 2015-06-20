package com.ozay.model;

/**
 * Created by naofumiezaki on 6/18/15.
 */
public class Permission {
    private String name;
    private String label;
    private long type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
