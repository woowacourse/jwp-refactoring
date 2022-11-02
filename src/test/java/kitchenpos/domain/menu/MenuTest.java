package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 생성자")
    @Nested
    class Constructor {

        @DisplayName("가격이 0보다 작다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceIsLessThan0() {
            // given
            MenuProduct menuProduct = new MenuProduct();
            MenuGroup menuGroup = new MenuGroup("메뉴 그룹");

            // when & then
            assertThatThrownBy(() -> new Menu("메뉴", new Price(BigDecimal.valueOf(-1)), menuGroup, List.of(menuProduct)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu 가격이 MenuProduct 들의 가격 * 수량 합보다 크다면, IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuPriceIsGreaterThanSumOfProductOfPriceAndQuantity() {
            // given
            MenuGroup menuGroup = new MenuGroup("메뉴 그룹");

            MenuProduct menuProduct1 = new MenuProduct(new Product("상품1", BigDecimal.valueOf(10_000)), 5L);
            MenuProduct menuProduct2 = new MenuProduct(new Product("상품2", BigDecimal.valueOf(50_000)), 1L);

            // when & then
            assertThatThrownBy(
                    () -> new Menu("메뉴", new Price(BigDecimal.valueOf(200_000)), menuGroup,
                            List.of(menuProduct1, menuProduct2)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
