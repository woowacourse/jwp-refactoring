package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixtures.Fixtures;
import kitchenpos.product.Product;
import kitchenpos.product.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    Fixtures fixtures;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록한다() {
            // given
            ProductRequest request = new ProductRequest("햄버거", BigDecimal.valueOf(10_000L));

            // when
            ProductResponse result = productService.create(request);

            // then
            assertThat(result.getName()).isEqualTo(request.getName());
            assertThat(result.getPrice()).isEqualTo(request.getPrice());
        }

    }

    @Test
    void 모든_상품_목록을_불러온다() {
        // given
        Product product = fixtures.상품_저장("햄버거", 10_000L);

        // when
        List<ProductResponse> results = productService.list();

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo(product.getName());
        assertThat(results.get(0).getPrice()).isEqualTo(product.getPrice().getValue());
    }

}
