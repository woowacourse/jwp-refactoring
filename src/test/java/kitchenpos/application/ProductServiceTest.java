package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

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

        @DisplayName("Product의 price가 null인 경우 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceOfProductIsNull() {
            // given
            ProductRequest request = new ProductRequest("상품", null);

            // when & then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
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
                Product product = new Product("product " + i, new BigDecimal(10_000));
                productRepository.save(product);
            }

            // when
            List<ProductResponse> actual = productService.list();

            // then
            assertThat(actual).hasSize(expected);
        }
    }
}
