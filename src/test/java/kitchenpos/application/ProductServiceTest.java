package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    class create_메소드는 {

        @Nested
        class null인_가격의_상품이_입력된_경우 {

            private final BigDecimal NULL_PRICE = null;

            private final Product product = new Product("후라이드", NULL_PRICE);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
            }
        }

        @Nested
        class 음수인_가격의_상품이_입력된_경우 {

            private final BigDecimal MINUS_PRICE = BigDecimal.valueOf(-1);

            private final Product product = new Product("후라이드", MINUS_PRICE);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
            }
        }

        @Nested
        class 상품을_정상적으로_생성가능한_경우 {

            private final Product product = new Product("후라이드", BigDecimal.valueOf(16000));

            @Test
            void 저장된_상품이_반환된다() {
                Product actual = productService.create(product);
                assertThat(actual).isNotNull();
            }
        }
    }
}
