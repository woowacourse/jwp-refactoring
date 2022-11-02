package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.request.ProductRequest;
import kitchenpos.product.application.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        ProductRequest request = new ProductRequest("name", BigDecimal.valueOf(1000));

        ProductResponse response = productService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void createThrowExceptionWhenPriceNegative() {
        assertThatThrownBy(() -> productService.create(new ProductRequest("name", BigDecimal.valueOf(-10))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("product의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    void list() {
        List<ProductResponse> response = productService.list();

        assertThat(response.size()).isEqualTo(6);
    }
}
