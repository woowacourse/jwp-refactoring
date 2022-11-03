package kitchenpos.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CustomCollectorTest {

    static class A {
        private Long id;

        public A(Long id) {
            this.id = id;
        }

        public Long getAId() {
            return id;
        }

        @Override
        public String toString() {
            return "A{" +
                    "id=" + id +
                    '}';
        }
    }

    static class B {
        private Long id;

        public B(Long id) {
            this.id = id;
        }

        public Long getBId() {
            return id;
        }

        @Override
        public String toString() {
            return "B{" +
                    "id=" + id +
                    '}';
        }
    }

    @Test
    void collect() {
        // given
        List<A> listA = List.of(new A(1L), new A(2L), new A(3L));
        List<B> listB = List.of(new B(2L), new B(3L), new B(5L));

        // when
        Map<A, B> result = listA.stream()
                .collect(CustomCollector.associate(listB, A::getAId, B::getBId));

        // then
        assertThat(result).hasSize(3);
    }
}
