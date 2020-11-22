package kitchenpos.util;

import java.util.Objects;

public class ValidateUtil {
    private ValidateUtil() {
    }

    public static void validateNonNull(Object... objects) {
        for (Object obj : objects) {
            if (Objects.isNull(obj)) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void validateNonNullAndNotEmpty(String... strings) {
        for (String st : strings) {
            if (Objects.isNull(st) || st.trim().isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
    }
}
