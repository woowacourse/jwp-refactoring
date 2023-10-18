package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.간장치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceIntegrationTest {

    @Nested
    @DisplayName("Product를 추가한다.")
    class Create {

        @Test
        @DisplayName("정상적으로 추가된다.")
        void success() {
            final Product product = new Product("test", BigDecimal.valueOf(20.00));

            final Product savedProduct = productService.create(product);

            assertAll(
                () -> assertThat(savedProduct.getPrice())
                    .isEqualByComparingTo(product.getPrice()),
                () -> assertThat(savedProduct.getName())
                    .isEqualTo(product.getName())
            );
        }

        //추후 파라미터가 dto로 변경되었을 때 테스트
//        @Test
//        void throwExceptionPriceIsNull() {
//            final Product product = new Product("test", null);
//
//            assertThatThrownBy(() -> productService.create(product))
//                .isInstanceOf(IllegalArgumentException.class);
//        }

//        @Test
//        @DisplayName("price가 0보다 작은 Exception을 throw한다.")
//        void throwExceptionPriceIsLowerThan0() {
//            final Product product = new Product("test", BigDecimal.valueOf(-1));
//
//            assertThatThrownBy(() -> productService.create(product))
//                .isInstanceOf(IllegalArgumentException.class);
//        }
    }

    @Test
    @DisplayName("product 목록을 반환할 수 있다.")
    void list() {
        final Product savedProduct = productService.create(간장치킨());

        final List<Product> products = productService.list();

        assertThat(products)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "price")
            .containsExactlyInAnyOrder(
                savedProduct
            );
    }
}
