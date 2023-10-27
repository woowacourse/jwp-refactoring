package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {
    @Test
    @DisplayName("가격 정보는 비어있거나 음수의 값이 아니어야 한다.")
    void whenInvalidPrice() {
        assertThatThrownBy(() -> new Price(null))
                .as("가격이 비어있는 경우 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Price(new BigDecimal("-1")))
                .as("가격이 음수인 경우에도 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 소수점 2자리를 포함해 총 19자리까지 표현할 수 있다.")
    void priceRange() {
        assertThatThrownBy(() -> new Price(new BigDecimal("123451234512345123.12")))
                .as("가격의 자릿수가 19자리를 초과하면 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);

        assertThatCode(() -> new Price(new BigDecimal("12345123451234512.34")))
                .doesNotThrowAnyException();
    }
}
