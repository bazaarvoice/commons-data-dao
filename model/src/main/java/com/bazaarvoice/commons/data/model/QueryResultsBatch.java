package com.bazaarvoice.commons.data.model;

public class QueryResultsBatch<T> {
    private Iterable<T> _values;
    private int _startIndex;
    private int _limit;
    private int _total;

    public QueryResultsBatch(Iterable<T> values, int startIndex, int limit, int total) {
        _values = values;
        _startIndex = startIndex;
        _limit = limit;
        _total = total;
    }

    public Iterable<T> getValues() {
        return _values;
    }

    /**
     * @return The starting point this query ran from to retrieve the current batch, with 0 representing the beginning
     */
    public int getStartIndex() {
        return _startIndex;
    }

    /**
     * @return The maximum number of results that could be available within the values iterable, which will be anywhere between 0 and this number
     */
    public int getLimit() {
        return _limit;
    }

    /**
     * @return The total number of results that were available for this query at the point it ran.
     * <p></p>
     * If there are multiple pages of results available, this number will exceed the size of the current batch available via this wrapper
     */
    public int getTotal() {
        return _total;
    }
}
