package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest {

    @DisplayName("create 메소드는 ")
    @Nested
    class CreateMethod {

        @DisplayName("Product를 생성한다.")
        @Test
        void Should_CreateProduct() {
            // given
            ProductRequest request = new ProductRequest("상품", BigDecimal.valueOf(10_000));

            // when
            ProductResponse actual = productService.create(request);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getName()).isEqualTo(request.getName());
                assertThat(actual.getPrice().doubleValue()).isEqualTo(request.getPrice().doubleValue());
            });
        }

        @DisplayName("Proudct의 price가 0보다 작을 경우 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceLessThan0() {
            // given
            ProductRequest request = new ProductRequest("상품", BigDecimal.valueOf(-10_000));

            // when & then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("저장된 전체 상품 목록을 반환한다.")
        @Test
        void Should_ReturnAllProductList() {
            // given
            int expected = 3;
            for (int i = 0; i < expected; i++) {
                saveProduct("product " + i, 10_000);
            }

            // when
            List<ProductResponse> actual = productService.list();

            // then
            assertThat(actual).hasSize(expected);
        }
    }
}
