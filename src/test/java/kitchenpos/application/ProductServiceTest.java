package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.support.IntegrationServiceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    @Nested
    class create_메서드는 extends IntegrationServiceTest {

        @Nested
        class 상품가에_null을_입력할_경우 {

            private final BigDecimal NULL_PRICE = null;
            private final Product product = new Product("간장",  NULL_PRICE);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }
    }

    @Nested
    class list_메서드는 extends IntegrationServiceTest {

        @Test
        void 상품목록을_반환한다() {
            List<Product> actual = productService.list();

            assertThat(actual).hasSize(6);
        }
    }
}