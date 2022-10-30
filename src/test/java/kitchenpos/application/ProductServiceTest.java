package kitchenpos.application;

import static kitchenpos.support.ProductFixture.상품;
import static kitchenpos.support.ProductFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.application.dto.ProductRequestDto;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

class ProductServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드를 테스트한다")
    class create_test {

        @Test
        @DisplayName("상품을 생성한다.")
        void create() {
            final ProductRequestDto product = 상품_생성("상품명", BigDecimal.valueOf(10000));

            final Product actual = 상품_등록(product);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo("상품명"),
                    () -> assertThat(actual.getPrice().intValue()).isEqualTo(10000)
            );

        }

        @Test
        @DisplayName("가격이 null이면 예외를 발생시킨다.")
        void create_nullPrice() {
            final ProductRequestDto product = 상품_생성("상품명", null);

            assertThatThrownBy(() -> 상품_등록(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수이면 예외를 발생시킨다.")
        void create_negativePrice() {
            final ProductRequestDto product = 상품_생성("상품명", BigDecimal.valueOf(-10000));

            assertThatThrownBy(() -> 상품_등록(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    @DisplayName("현재 등록된 물품들을 반환한다.")
    void list() {
        상품_등록(상품);
        상품_등록(상품);
        상품_등록(상품);

        final List<Product> actual = productService.list();

        assertThat(actual).hasSize(3);

    }
}
