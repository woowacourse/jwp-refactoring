package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuTest {

    @DisplayName("메뉴 생성 테스트")
    @Nested
    class MenuCreateTest {

        @DisplayName("메뉴를 생성한다.")
        @Test
        void menuCreateTest() {
            //given
            final String menuName = "menu";
            final BigDecimal price = new BigDecimal(1000);
            final long menuGroupId = 1L;
            final Product product = new Product("product", new BigDecimal(3000));
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(product, 4));

            //when
            final Menu menu = new Menu(menuName, price, menuGroupId, menuProducts);

            //then
            assertSoftly(softly -> {
                softly.assertThat(menu.getId()).isNull();
                softly.assertThat(menu.getName()).isEqualTo(menuName);
                softly.assertThat(menu.getPrice()).isEqualByComparingTo(price);
                softly.assertThat(menu.getMenuGroupId()).isEqualTo(menuGroupId);
                softly.assertThat(menu.getMenuProducts()).isEqualTo(menuProducts);
            });
        }

        @DisplayName("가격이 음수면 실패한다.")
        @Test
        void menuCreateFailWhenPriceLessThenZero() {
            //given
            final String menuName = "menu";
            final BigDecimal price = new BigDecimal(-1);
            final long menuGroupId = 1L;
            final List<MenuProduct> menuProducts = Collections.emptyList();

            // when & then
            assertThatThrownBy(() -> new Menu(menuName, price, menuGroupId, menuProducts))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 null이면 실패한다.")
        @Test
        void menuCreateFailWhenPriceIsNull() {
            //given
            final String menuName = "menu";
            final BigDecimal price = null;
            final long menuGroupId = 1L;
            final List<MenuProduct> menuProducts = Collections.emptyList();

            // when & then
            assertThatThrownBy(() -> new Menu(menuName, price, menuGroupId, menuProducts))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
