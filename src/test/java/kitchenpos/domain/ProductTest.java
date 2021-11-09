package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ProductTest {

    @Test
    @DisplayName("가격은 0 이상이어야 한다.")
    void validatePriceTest() {

        // given
        Product product = new Product(1L, "이름", BigDecimal.ZERO);

        // when
        ThrowableAssert.ThrowingCallable callable = product::validatePrice;

        // then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가격은 0 이상이어야 한다. 실패")
    void validatePriceFailTest() {

        // given
        Product product = new Product(1L, "이름", BigDecimal.valueOf(-1));

        // when
        ThrowableAssert.ThrowingCallable callable = product::validatePrice;

        // then
        assertThatIllegalArgumentException().isThrownBy(callable);
    }
}
