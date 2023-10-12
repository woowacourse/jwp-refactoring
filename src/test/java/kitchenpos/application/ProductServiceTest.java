package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.domain.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixtures.domain.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest extends ServiceTest{

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @DisplayName("create 메소드는 ")
    @Nested
    class CreateMethod {

        @DisplayName("Product를 생성한다.")
        @Test
        void Should_CreateProduct() {
            // given
            final Product product = new ProductFixture.ProductRequestBuilder().build();

            // when
            final Product actual = productService.create(product);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getName()).isEqualTo(product.getName());
                assertThat(actual.getPrice().doubleValue()).isEqualTo(product.getPrice().doubleValue());
            });
        }

        @DisplayName("Product의 price가 null인 경우 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceOfProductIsNull() {
            // given
            final Product product = new ProductFixture.ProductRequestBuilder()
                    .price(null)
                    .build();

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Proudct의 price가 0보다 작을 경우 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceLessThan0() {
            // given
            final Product product = new ProductFixture.ProductRequestBuilder()
                    .price(-10_000)
                    .build();

            // when & then
            assertThatThrownBy(() -> productService.create(product))
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
            final int expected = 3;
            for (int i = 0; i < expected; i++) {
                final Product product = createProduct("product " + i, new BigDecimal(10_000));
                productDao.save(product);
            }

            // when
            final List<Product> actual = productService.list();

            // then
            assertThat(actual).hasSize(expected);
        }
    }
}
