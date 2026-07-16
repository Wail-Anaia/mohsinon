package com.mohsinon.shared.query.criteria;

public class SearchCriteria {

    private String key;

    private FilterOperator operator;

    private Object value;

    private Object valueTo;

    public SearchCriteria() {
    }

    public SearchCriteria(
            String key,
            FilterOperator operator,
            Object value) {

        this.key = key;
        this.operator = operator;
        this.value = value;
    }

    public SearchCriteria(
            String key,
            FilterOperator operator,
            Object value,
            Object valueTo) {

        this.key = key;
        this.operator = operator;
        this.value = value;
        this.valueTo = valueTo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public void setOperator(FilterOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValueTo() {
        return valueTo;
    }

    public void setValueTo(Object valueTo) {
        this.valueTo = valueTo;
    }

}