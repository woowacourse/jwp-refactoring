package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Menu 는 ")
class MenuTest {

    @DisplayName("메뉴를 생성할 때 메뉴 상품들의 총 금액에비해 메뉴의 금액이 ")
    @Nested
    class MenuCreationTest {

        @DisplayName("같거나 적으면 메뉴를 생성한다(적다는 의미는 할인이 적용되었다는 의미).")
        @Test
        void createMenuSuccess() {
            final Product product = new Product(1L, "productName", BigDecimal.valueOf(1000L));
            final MenuProduct menuProduct = new MenuProduct(1L, 1L, product, 2);

            assertDoesNotThrow(() -> new Menu("menuName", BigDecimal.valueOf(1999L), 1L, List.of(menuProduct)));
        }

        @DisplayName("더 크면 에러를 던진다.")
        @Test
        void creatMenuFail() {
            final Product product = new Product(1L, "productName", BigDecimal.valueOf(1000L));
            final MenuProduct menuProduct = new MenuProduct(1L, 1L, product, 2);

            assertThatThrownBy(() -> new Menu("menuName", BigDecimal.valueOf(2001L), 1L, List.of(menuProduct)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
