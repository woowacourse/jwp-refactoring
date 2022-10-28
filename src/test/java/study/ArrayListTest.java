package study;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ArrayListTest {

    @Test
    void addAll() {
        final ArrayList<Integer> integers = new ArrayList<>();
        final ArrayList<Integer> integersToAdd = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integersToAdd.add(i);
        }

        integers.addAll(integersToAdd);

        assertThat(integers).hasSize(10);
    }
}
