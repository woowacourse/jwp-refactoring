package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import kitchenpos.ui.dto.response.ProductCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ProductServiceTest extends ServiceTestConfig {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("상품이름", BigDecimal.valueOf(10000));

            // when
            final ProductCreateResponse actual = productService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getName()).isEqualTo(request.getName());
                softly.assertThat(actual.getPrice().compareTo(request.getPrice())).isZero();
            });
        }

        @DisplayName("가격이 null 이면 실패한다.")
        @Test
        void fail_if_price_is_null() {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("상품이름", null);

            // then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 미만이면 실패한다.")
        @Test
        void fail_if_price_under_zero() {
            // given
            final ProductCreateRequest request = new ProductCreateRequest("상품이름", BigDecimal.valueOf(-1));

            // then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 전체 조회")
    @Nested
    class ReadAll {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final Product savedProduct = saveProduct();

            // when
            final List<Product> actual = productService.list();

            // then
            // FIXME: equals&hashcode 적용
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
//                softly.assertThat(actual).containsExactly(savedProduct);
            });
        }
    }
}
