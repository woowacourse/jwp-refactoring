package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = BigDecimal.valueOf(5000);
            private final Product product = new Product(name, price);

            @Test
            void 상품을_추가한다() {
                Product actual = productService.create(product);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getName()).isEqualTo(name);
                    assertThat(actual.getPrice()).isCloseTo(price, HUNDRED_PERCENT);
                });
            }
        }

        @Nested
        class 상품의_가격이_null_일_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = null;
            private final Product product = new Product(name, price);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("상품의 가격은 null 이거나 0원 미만일 수 없습니다.");
            }
        }

        @Nested
        class 상품의_가격이_0원_미만일_경우 {

            private final String name = "햄버거";
            private final BigDecimal price = BigDecimal.valueOf(-1);
            private final Product product = new Product(name, price);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("상품의 가격은 null 이거나 0원 미만일 수 없습니다.");
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            @Test
            void 상품_목록을_반환한다() {
                List<Product> products = productService.list();

                assertThat(products).isNotEmpty();
            }
        }
    }
}
