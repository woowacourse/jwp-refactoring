package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import common.IntegrationTest;
import java.math.BigDecimal;
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
        Product productRequest = createProductRequest("국밥", BigDecimal.ONE.negate());

        assertThatThrownBy(() -> sut.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 null일 수 없다.")
    @Test
    void priceMustNotNull() {
        Product productRequest = createProductRequest("국밥", null);

        assertThatThrownBy(() -> sut.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Product createProductRequest(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
