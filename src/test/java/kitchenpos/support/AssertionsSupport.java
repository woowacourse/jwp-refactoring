package kitchenpos.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.MultipleFailuresError;

public class AssertionsSupport extends Assertions {

    public static void assertAll(final Executable executable1, final Collection<Executable>... executableCollections) throws MultipleFailuresError {

        final List<Executable> executables = decrease1Dimension(executableCollections);
        executables.add(executable1);

        assertAll(executables);
    }

    public static void assertAll(final Executable executable1, final Executable executable2, final Collection<Executable>... executableCollections) throws MultipleFailuresError {

        final List<Executable> executables = decrease1Dimension(executableCollections);
        executables.add(executable1);
        executables.add(executable2);

        assertAll(executables);;
    }

    public static void assertAll(final Executable executable1, final Executable executable2, final Executable executable3, final Collection<Executable>[] executableCollections) throws MultipleFailuresError {

        final List<Executable> executables = decrease1Dimension(executableCollections);
        executables.add(executable1);
        executables.add(executable2);
        executables.add(executable3);

        assertAll(executables);;
    }

    /**
     * 2차원의 Executable을 1차원으로 변경합니다.
     *
     * @param executableCollections 2차원
     * @return 1차원
     */
    private static List<Executable> decrease1Dimension(final Collection<Executable>[] executableCollections) {
        return Arrays.stream(executableCollections)
                .flatMap(executableCollection -> executableCollection.stream())
                .collect(Collectors.toList());
    }

    private static void assertAll(final List<Executable> executables) {
        assertAll(executables.toArray(Executable[]::new));
    }
}
