package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.IntegrationTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ProductRestControllerTest {

    @Autowired
    private ProductRestController sut;

    @DisplayName("가격은 0원 미만일 수 없다.")
    @Test
    void priceMustOverZero() {
        Product product = createProduct("국밥", BigDecimal.ONE.negate());

        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 null일 수 없다.")
    @Test
    void priceMustNotNull() {
        Product product = createProduct("국밥", null);

        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
