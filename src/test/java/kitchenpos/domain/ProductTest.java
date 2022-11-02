package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("생성할 때")
    @Nested
    class New {

        @DisplayName("성공")
        @Test
        void success() {
            Product product = new Product("이름", 1L);
            assertThat(product).isNotNull();
        }

        @DisplayName("가격의 null이면 예외를 발생시킨다.")
        @Test
        void priceIsNull_exception() {
            assertThatThrownBy(() -> new Product("이름", null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0보다 작으면 예외를 발생시킨다.")
        @Test
        void priceLessThanZero_exception() {
            assertThatThrownBy(() -> new Product("이름", -1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
