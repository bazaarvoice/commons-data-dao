package com.bazaarvoice.commons.data.model;

import java.util.List;

public interface QueryResults<T> {
    List<T> getValues();
    int getStartIndex();
    int getTotal();
}
