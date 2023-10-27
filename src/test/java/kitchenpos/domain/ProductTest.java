package kitchenpos.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("가격이 음수인 제품 생성 시 예외가 발생한다.")
    void failToGenerateProductWithNullPrice() {
        // given
        String productName = "치킨";
        BigDecimal nullPrice = null;

        // when & then
        assertThatThrownBy(() -> new Product(productName, nullPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수인 제품 생성 시 예외가 발생한다.")
    void failToGenerateProductWithMinusPrice() {
        // given
        String productName = "치킨";
        BigDecimal wrongPrice = BigDecimal.valueOf(-1);

        // when & then
        assertThatThrownBy(() -> new Product(productName, wrongPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
