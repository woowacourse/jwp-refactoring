package kitchenpos.domain;

import static kitchenpos.common.constants.Constants.야채곱창_이름;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("상품 가격이 0원 보다 작으면 예외가 발생한다.")
    @Test
    void 상품_가격이_0원_보다_작으면_예외가_발생한다() {
        // given
        BigDecimal 잘못된_가격 = BigDecimal.valueOf(-1L);

        // when & then
        assertThatThrownBy(() -> new Product(야채곱창_이름, 잘못된_가격))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상의 정수로 입력해주세요.");
    }

    @DisplayName("상품 가격이 null 이면 예외가 발생한다.")
    @Test
    void 상품_가격이_null_이면_예외가_발생한다() {
        // given
        BigDecimal 잘못된_가격 = null;

        // when & then
        assertThatThrownBy(() -> new Product(야채곱창_이름, 잘못된_가격))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상의 정수로 입력해주세요.");
    }
}