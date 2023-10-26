package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.product.request.ProductCreateRequest;
import kitchenpos.dto.product.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Nested
    @DisplayName("상품을 등록할 때, ")
    class CreateProduct {
        @Test
        @DisplayName("정상 등록된다")
        void createProduct() {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("name", BigDecimal.valueOf(1000));

            // when
            final Long productId = productService.create(request);

            // then
            assertThat(productId).isPositive();
        }

        @Test
        @DisplayName("상품 가격이 null일 시 예외 발생")
        void productPriceNullException() {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("name", null);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> productService.create(request));
        }

        @Test
        @DisplayName("상품 가격이 0원 미만일 시 예외 발생")
        void productPriceLessThanZeroWonException() {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("name", BigDecimal.valueOf(-1));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> productService.create(request));
        }
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void getProducts() {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("name", BigDecimal.valueOf(1000));
        productService.create(request);

        // when
        final List<ProductResponse> responses = productService.list();

        // then
        assertThat(responses).isNotEmpty();
    }
}
