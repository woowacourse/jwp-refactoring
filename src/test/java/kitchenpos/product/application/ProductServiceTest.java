package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.test.ServiceTest;
import org.assertj.core.util.BigDecimalComparator;
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
    class 상품_목록_조회_시 {

        @Test
        void 모든_상품_목록을_조회한다() {
            //given
            ProductCreateRequest productCreateRequestA = new ProductCreateRequest("텐동", BigDecimal.valueOf(11000));
            ProductCreateRequest productCreateRequestB = new ProductCreateRequest("사케동", BigDecimal.valueOf(12000));
            ProductResponse productResponseA = productService.create(productCreateRequestA);
            ProductResponse productResponseB = productService.create(productCreateRequestB);

            //when
            List<ProductResponse> products = productService.list();

            //then
            assertThat(products).usingRecursiveComparison()
                    .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                    .isEqualTo(List.of(productResponseA, productResponseB));
        }

        @Test
        void 상품이_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<ProductResponse> products = productService.list();

            //then
            assertThat(products).isEmpty();
        }
    }

    @Nested
    class 상품_추가_시 {

        @Test
        void 정상적인_상품이라면_상품을_추가한다() {
            //given
            ProductCreateRequest productCreateRequest = new ProductCreateRequest("텐동", BigDecimal.valueOf(11000));

            //when
            ProductResponse productResponse = productService.create(productCreateRequest);

            //then
            assertSoftly(softly -> {
                softly.assertThat(productResponse.getId()).isNotNull();
                softly.assertThat(productResponse.getName()).isEqualTo("텐동");
                softly.assertThat(productResponse.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(11000));
            });
        }

        @Test
        void 가격이_NULL이라면_예외를_던진다() {
            //given
            ProductCreateRequest productCreateRequest = new ProductCreateRequest("텐동", null);

            //when, then
            assertThatThrownBy(() -> productService.create(productCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {Integer.MIN_VALUE, -1})
        void 가격이_0보다_작으면_예외를_던진다(long price) {
            //given
            ProductCreateRequest productCreateRequest = new ProductCreateRequest("텐동", BigDecimal.valueOf(price));

            //when, then
            assertThatThrownBy(() -> productService.create(productCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
