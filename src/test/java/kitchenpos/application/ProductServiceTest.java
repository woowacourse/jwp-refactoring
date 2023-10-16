package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    class 상품_생성_ {

        @Test
        void 정상_요청() {
            // given
            ProductCreateRequest request = new ProductCreateRequest("피움 치킨", 18_000L);

            // when
            Product savedProduct = productService.create(request);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(savedProduct.getPrice().longValue()).isEqualTo(request.getPrice());
                        softly.assertThat(savedProduct.getName()).isEqualTo(request.getName());
                    }
            );
        }

        @Test
        void 가격없이_요청하면_예외_발생() {
            // given
            ProductCreateRequest request = new ProductCreateRequest("조이 치킨", null);

            // when, then
            assertThatThrownBy(
                    () -> productService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {-2L, -100L})
        void 가격이_0미만이면_예외_발생(long price) {
            // given
            ProductCreateRequest request = new ProductCreateRequest("조이 치킨", price);

            // when, then
            assertThatThrownBy(
                    () -> productService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 전체_상품_조회_ {

        @Test
        void 정상_요청() {
            // given
            ProductCreateRequest request = new ProductCreateRequest("조이 치킨", 18_000L);
            Product savedProduct = productService.create(request);

            // when
            List<Product> products = productService.readAll();

            // then
            assertThat(products)
                    .extracting(Product::getId)
                    .contains(savedProduct.getId());
        }
    }
}
