package kitchenpos.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class RequestCreator {

    public static  <T> T getObject(final Class<T> requestClass, final Object... input)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Constructor<T> defaultConstructor = requestClass.getDeclaredConstructor();
        defaultConstructor.setAccessible(true);
        final T request = defaultConstructor.newInstance();
        final Field[] fields = requestClass.getDeclaredFields();
        for (int i = 0; i < input.length; i++) {
            fields[i].setAccessible(true);
            fields[i].set(request, input[i]);
        }
        return request;
    }
}
