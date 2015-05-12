package com.bazaarvoice.commons.data.dao.json;

public interface DelegateItemType<T> {

    Class<? extends T> getDelegateItemClass();

}
