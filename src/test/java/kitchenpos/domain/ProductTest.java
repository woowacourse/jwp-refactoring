package kitchenpos.domain;

import kitchenpos.exception.BadRequestException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("프로덕트의 가격은 null이거나 0 미만일 수 없다")
    @Test
    void productPriceCannotBeNullOrZero() {
        assertThatThrownBy(() ->
                new Product.Builder()
                        .price(null)
                        .build())
                .isInstanceOf(BadRequestException.class);

        assertThatThrownBy(() ->
                new Product.Builder()
                        .price(BigDecimal.valueOf(-10000))
                        .build())
                .isInstanceOf(BadRequestException.class);
    }
}
