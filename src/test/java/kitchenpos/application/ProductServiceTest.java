package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 유효한_상품이_입력되면 extends ServiceTest {

            private final Product product = new Product("치킨", new BigDecimal(100));

            @Test
            void 상품을_반환한다() {
                final Product savedProduct = productService.create(product);

                assertThat(savedProduct.getId()).isNotNull();
            }
        }

        @Nested
        class 가격이_null인_상품이_입력되면 extends ServiceTest {

            private final Product product = new Product("치킨", null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 가격이_0_미만인_상품이_입력되면 extends ServiceTest {

            private final Product product = new Product("치킨", new BigDecimal(-1));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되면 extends ServiceTest {

            @Test
            void 모든_상품을_반환한다() {
                final List<Product> products = productService.list();

                assertThat(products).isEmpty();
            }
        }
    }
}
