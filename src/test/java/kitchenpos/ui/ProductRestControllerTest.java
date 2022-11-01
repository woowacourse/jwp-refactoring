package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import common.IntegrationTest;
import java.math.BigDecimal;
import kitchenpos.ui.request.ProductRequest;
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
        assertThatThrownBy(() -> createProduct("국밥", BigDecimal.ONE.negate()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 null일 수 없다.")
    @Test
    void priceMustNotNull() {
        assertThatThrownBy(() -> createProduct("국밥", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void createProduct(String name, BigDecimal price) {
        ProductRequest request = new ProductRequest(name, price);
        sut.create(request);
    }
}
