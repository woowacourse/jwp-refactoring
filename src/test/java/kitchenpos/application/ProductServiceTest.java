package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ProductService 클래스의")
class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("list 메서드는 모든 product를 조회한다.")
    void list() {
        // given
        saveProduct("크림치킨", BigDecimal.valueOf(10000.00));
        saveProduct("크림어니언치킨", BigDecimal.valueOf(15000));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("product를 생성한다.")
        void success() {
            // given
            Product product = new Product();
            product.setName("크림치킨");
            product.setPrice(BigDecimal.valueOf(10000.00));

            // when
            Product savedProduct = productService.create(product);

            // then
            Optional<Product> actual = productDao.findById(savedProduct.getId());
            assertThat(actual).isPresent();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-1"})
        @DisplayName("price가 null이거나 0보다 작은 경우 예외를 던진다.")
        void price_isNull_ExceptionThrown(BigDecimal price) {
            // given
            Product product = createProduct("크림치킨", price);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
