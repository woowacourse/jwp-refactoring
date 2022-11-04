package kitchenpos.application.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.dto.product.request.ProductCreateRequest;
import kitchenpos.dto.product.response.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성할_수_있다() {
        ProductCreateRequest request = new ProductCreateRequest("상품", BigDecimal.ZERO);

        ProductResponse response = productService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        Long productId = productService.create(new ProductCreateRequest("상품", BigDecimal.ZERO))
                .getId();

        List<ProductResponse> actual = productService.list();

        Assertions.assertThat(actual).hasSize(1)
                .extracting("id")
                .containsOnly(productId);
    }
}
