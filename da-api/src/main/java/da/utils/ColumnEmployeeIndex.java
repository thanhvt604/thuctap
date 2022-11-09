package com.globits.da.utils;

public enum ColumnEmployeeIndex {

    CODE(0),
    NAME(1),
    EMAIL(2),
    PHONE_NUMBER(3),
    AGE(4),
    PROVINCE(5),
    DISTRICT(6),
    COMMUNE(7);

    private int index;
    ColumnEmployeeIndex(int index){ this.index=index;}

    public int getIndex() {
        return index;
    }
}
