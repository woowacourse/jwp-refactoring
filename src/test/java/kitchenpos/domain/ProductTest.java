package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.exception.ProductException.InvalidProductNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품을 생성할 때 이름이 null이면 예외가 발생한다.")
    void init_fail1() {
        assertThatThrownBy(() -> Product.of(null, BigDecimal.valueOf(1000)))
                .isInstanceOf(InvalidProductNameException.class);
    }

    @Test
    @DisplayName("상품을 생성할 때 이름이 빈 문자열이면 예외가 발생한다.")
    void init_fail2() {
        assertThatThrownBy(() -> Product.of("", BigDecimal.valueOf(1000)))
                .isInstanceOf(InvalidProductNameException.class);
    }
}
