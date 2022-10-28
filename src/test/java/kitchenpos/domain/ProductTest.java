package kitchenpos.domain;

import static kitchenpos.common.constants.Constants.야채곱창_이름;
import static kitchenpos.common.constants.Constants.잘못된_가격;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.builder.ProductBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품 가격이 0원 보다 작으면 예외가 발생한다.")
    @Test
    void 상품_가격이_0원_보다_작으면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> 상품_생성(야채곱창_이름, 잘못된_가격))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 null 이면 예외가 발생한다.")
    @Test
    void 상품_가격이_null_이면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> 상품_생성(야채곱창_이름, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Product 상품_생성(final String name, final BigDecimal price) {
        return new ProductBuilder()
                .name(name)
                .price(price)
                .build();
    }
}