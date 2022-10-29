package kitchenpos.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.MultipleFailuresError;

public class AssertionsSupport extends Assertions {

    public static void assertAll(Executable executable1, Collection<Executable> executables) throws MultipleFailuresError {

        executables.add(executable1);

        assertAll(executables);
    }

    public static void assertAll(Executable executable1, Executable executable2, Collection<Executable> executables) throws MultipleFailuresError {

        executables.add(executable1);
        executables.add(executable2);

        assertAll(executables);;
    }

    public static void assertAll(Executable executable1, Executable executable2, Executable executable3, Collection<Executable> executables) throws MultipleFailuresError {

        executables.add(executable1);
        executables.add(executable2);
        executables.add(executable3);

        assertAll(executables);
    }

    public static void assertAll(Executable executable1,
                                 Executable executable2,
                                 Executable executable3,
                                 Executable executable4,
                                 Collection<Executable> executables
    ) throws MultipleFailuresError {

        executables.add(executable1);
        executables.add(executable2);
        executables.add(executable3);
        executables.add(executable4);

        assertAll(executables);
    }

    public static void assertAll(Executable executable1, 
                                 Executable executable2, 
                                 Executable executable3, 
                                 Executable executable4, 
                                 Executable executable5, 
                                 Collection<Executable> executables
    ) throws MultipleFailuresError {

        executables.add(executable1);
        executables.add(executable2);
        executables.add(executable3);
        executables.add(executable4);
        executables.add(executable5);

        assertAll(executables);
    }

    /**
     * 현재는 사용되지 않습니다.
     * <p/>
     * 2차원의 Executable을 1차원으로 변경합니다.
     *
     * @param executables 2차원
     * @return 1차원
     */
    private static List<Executable> decrease1Dimension(Collection<Executable>[] executableCollections) {
        return Arrays.stream(executableCollections)
                .flatMap(executables -> executables.stream())
                .collect(Collectors.toList());
    }

    private static void assertAll(List<Executable> executables) {
        assertAll(executables.toArray(Executable[]::new));
    }
}
