package ch.avocado.share.service.Mock;

/**
 * Created by coffeemakr on 23.03.16.
 */

import ch.avocado.share.model.data.AccessControlObjectBase;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class DataHandlerMockBase<E extends AccessControlObjectBase>{
    protected Map<String, E> objects;


    public DataHandlerMockBase() {
        objects = new HashMap<>();
    }

    protected E get(String id) {
        if(id == null) throw new IllegalArgumentException("id is null");
        return objects.get(id);
    }

    protected String add(E object) {
        if(object == null) throw new IllegalArgumentException("object is null");
        if(object.getId() != null) throw new IllegalArgumentException("object.getId() is not null");
        Random random = new Random();
        object.setId("object_" + random.nextLong() + random.nextLong());
        objects.put(object.getId(), object);
        System.out.println("Created object with id " + object.getId());
        return object.getId();
    }

    protected boolean delete(E object) {
        if(objects.containsKey(object.getId())) {
            objects.remove(object.getId());
            return true;
        }
        return false;
    }

    protected boolean update(E object) {
        if(object == null) throw new IllegalArgumentException("object is null");
        if(objects.containsKey(object.getId())) {
            objects.put(object.getId(), object);
            return true;
        }
        return false;
    }

    protected E[] getAll(Class<E> type) {
        E[] objectArray = (E[]) Array.newInstance(type, objects.size());
        return objects.values().toArray(objectArray);
    }
}
