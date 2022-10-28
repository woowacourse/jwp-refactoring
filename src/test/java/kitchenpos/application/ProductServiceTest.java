package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    @Nested
    class CreateTest extends ServiceTest {

        @Test
        void create_fail_when_price_is_null() {
            final ProductRequest productRequest = new ProductRequest("순삭치킨", null);

            assertThatThrownBy(() -> productService.create(productRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_price_is_smaller_than_zero() {
            final ProductRequest productRequest = new ProductRequest("순삭치킨", BigDecimal.valueOf(-1));

            assertThatThrownBy(() -> productService.create(productRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_success() {
            final ProductRequest productRequest = new ProductRequest("순삭치킨", BigDecimal.valueOf(10000));

            assertThatThrownBy(() -> productService.create(productRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ListTest extends ServiceTest {

        @Test
        void list() {
            assertThat(productService.list()).hasSize(6);
        }
    }
}