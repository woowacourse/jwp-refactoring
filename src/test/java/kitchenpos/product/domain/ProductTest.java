package kitchenpos.product.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ProductTest {

    @Test
    @DisplayName("Price의 값이 음수일 때 상품을 생성하면 예외를 반환한다.")
    void Product_fail_negative() {
        //given, when
        final ThrowingCallable actual = () -> new Product(null, null,
                new Price(BigDecimal.valueOf(-3000)));

        //then
        Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Price의 값이 null일 때 상품을 생성하면 예외를 반환한다.")
    void Product_fail_null() {
        //given, when
        final ThrowingCallable actual = () -> new Product(null, null,
                new Price(null));

        //then
        Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

}