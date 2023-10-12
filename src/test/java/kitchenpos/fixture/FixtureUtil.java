package kitchenpos.fixture;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FixtureUtil {

    public static <T> T idIgnored(T object) {
        try {
            Field id = Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(it -> it.getName().equals("id"))
                    .findFirst()
                    .get();
            id.setAccessible(true);
            id.set(object, null);
            return object;
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T, F> List<F> listAllFrom(Class<T> clazz, Class<F> fixtureClazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.getReturnType().isAssignableFrom(fixtureClazz))
                .map(staticMethod -> safeInvoke(staticMethod, fixtureClazz))
                .collect(Collectors.toList());
    }

    public static <T, F> List<F> listAllInDatabaseFrom(Class<T> clazz, Class<F> fixtureClazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(InDatabase.class))
                .map(staticMethod -> safeInvoke(staticMethod, fixtureClazz))
                .collect(Collectors.toList());
    }

    private static <T> T safeInvoke(Method staticMethod, Class<T> fixtureClazz) {
        try {
            return fixtureClazz.cast(staticMethod.invoke(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T pushing(T target, Object... values) {
        try {
            Field[] fields = target.getClass().getDeclaredFields();
            validateLengthEquals(fields, values);

            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(target, values[i]);
            }
            return target;
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void validateLengthEquals(Field[] fields, Object[] values) {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("입력한 필드 개수가 실제와 다릅니다");
        }
    }
}
