package com.bazaarvoice.commons.data.dao.mongo;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

public class AbstractMongoFields {
    protected static class FieldDesignator {
        private final List<String> _fields = Lists.newArrayList();

        public FieldDesignator() {
        }

        public FieldDesignator field(String fieldName) {
            _fields.add(fieldName);
            return this;
        }

        public FieldDesignator arrayIndex(int index) {
            _fields.add(Integer.toString(index));
            return this;
        }

        /**
         * Used to update or return a particular field in an array that matches a query.
         *
         * See http://www.mongodb.org/display/DOCS/Updating#Updating-The%24positionaloperator
         */
        public FieldDesignator matchedArray() {
            _fields.add("$");
            return this;
        }

        public String build() {
            return Joiner.on('.').join(_fields);
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
