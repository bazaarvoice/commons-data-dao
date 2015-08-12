package com.bazaarvoice.commons.data.model;

import java.io.Serializable;
import java.util.List;

public class SimpleQueryResults<T> implements QueryResults<T>, Serializable {
    private List<T> _values;
    private int _startIndex;
    private int _total;

    private SimpleQueryResults() {}

    public SimpleQueryResults(List<T> values, int startIndex, int total) {
        _values = values;
        _startIndex = startIndex;
        _total = total;
    }

    @Override
    public List<T> getValues() {
        return _values;
    }

    @Override
    public int getStartIndex() {
        return _startIndex;
    }

    @Override
    public int getTotal() {
        return _total;
    }
}
