package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest{

    @Nested
    class CreateTest {

        Product product;

        @BeforeEach
        void setup() {
            product = new Product();
            product.setName("순삭치킨");
            product.setPrice(BigDecimal.valueOf(10000));
        }

        @Test
        void create_fail_when_price_is_null() {
            product.setPrice(null);

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_price_is_smaller_than_zero() {
            product.setPrice(BigDecimal.valueOf(-1));

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ListTest {

        @Test
        void list() {
            assertThat(productService.list()).hasSize(6);
        }
    }
}