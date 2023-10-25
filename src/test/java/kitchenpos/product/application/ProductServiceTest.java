package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.product.application.response.ProductResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    class 상품을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            ProductCreateRequest productCreateRequest = new ProductCreateRequest("벨리곰", 10_000);

            ProductResponse productResponse = productService.create(productCreateRequest);

            assertAll(
                    () -> assertThat(productResponse.getName()).isEqualTo(productCreateRequest.getName()),
                    () -> assertThat(productResponse.getPrice()).isEqualTo(
                            String.valueOf(productCreateRequest.getPrice()))
            );
        }

        @Test
        void 상품_가격이_음수이면_예외가_발생한다() {
            ProductCreateRequest productCreateRequest = new ProductCreateRequest("벨리곰", -1);

            assertThatThrownBy(() -> productService.create(productCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_상품을_조회한다() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("milk", 1_000);
        productService.create(productCreateRequest);

        List<ProductResponse> productResponses = productService.list();

        assertThat(productResponses).hasSizeGreaterThan(1);
    }
}
