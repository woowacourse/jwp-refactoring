package kitchenpos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public static <T> List<T> of(T ... t) {
        return Arrays.stream(t).collect(Collectors.toList());
    }
}
