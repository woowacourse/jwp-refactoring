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
}
