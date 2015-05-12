package test.integration.dao.impl;

import com.bazaarvoice.commons.data.dao.ModelDAORW;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import test.integration.dao.TestUser;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TestDummyUserDAORW implements ModelDAORW<TestUser> {
    Map<String, TestUser> _usersByID = Maps.newHashMap();

    @Override
    public TestUser get(String objectID) {
        return _usersByID.get(objectID);
    }

    @Override
    public List<TestUser> get(List<String> objectIDs) {
        return Lists.transform(objectIDs, new Function<String, TestUser>() {
            @Override
            public TestUser apply(@Nullable String objectID) {
                return get(objectID);
            }
        });
    }

    @Override
    public void save(TestUser object) {
        _usersByID.put(object.getID(), object);
    }

    @Override
    public void saveAll(Collection<TestUser> objects) {
        for (final TestUser object : objects) {
            save(object);
        }
    }

    @Override
    public void create(TestUser... objects) {
        saveAll(Arrays.asList(objects));
    }

    @Override
    public void create(Collection<TestUser> objects) {
        saveAll(objects);
    }

    @Override
    public void delete(TestUser object) {
        deleteByID(object.getID());
    }

    @Override
    public void deleteByID(String objectID) {
        _usersByID.remove(objectID);
    }

    @Override
    public void deleteAll(Collection<TestUser> objects) {
        for (final TestUser object : objects) {
            delete(object);
        }
    }

    @Override
    public void deleteAllByID(Collection<String> objectIDs) {
        for (final String objectID : objectIDs) {
            deleteByID(objectID);
        }
    }
}
