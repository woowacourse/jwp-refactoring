package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.exception.KitchenPosException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Nested
    class 생성 {

        @Test
        void 가격이_null_이면_예외() {
            // given
            Money price = null;
            MenuGroup menuGroup = new MenuGroup(1L, "주류");

            // when & then
            assertThatThrownBy(() -> new Menu(1L, "맥주세트", price, menuGroup))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("메뉴의 가격은 null이 될 수 없습니다.");
        }

        @Test
        void 가격이_0보다_작으면_예외() {
            // given
            Money price = Money.from(-1);
            MenuGroup menuGroup = new MenuGroup(1L, "주류");

            // when & then
            assertThatThrownBy(() -> new Menu(1L, "맥주세트", price, menuGroup))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("메뉴의 가격은 0보다 작을 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1, 1000})
        void 가격이_0_이상이면_성공(long value) {
            // given
            Money price = Money.from(value);
            MenuGroup menuGroup = new MenuGroup(1L, "주류");

            // when
            Menu menu = new Menu(1L, "맥주세트", price, menuGroup);

            // then
            assertThat(menu.getPrice().getAmount()).isNotNegative();
        }
    }

    @Nested
    class addMenuProducts {

        @Test
        void 메뉴_상품의_가격_총합보다_메뉴의_가격이_크면_예외() {
            // given
            MenuGroup menuGroup = new MenuGroup(1L, "주류");
            Menu menu = new Menu(1L, "맥주세트", Money.from(1100), menuGroup);

            List<MenuProduct> menuProducts = List.of(
                new MenuProduct(1L, 10, new Product(1L, "맥주", Money.from(100)), menu));

            // when & then
            assertThatThrownBy(() -> menu.addMenuProducts(menuProducts))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품의 총합 가격보다 작아야 합니다.");
        }

        @Test
        void 메뉴_상품의_가격_총합보다_메뉴의_가격이_작으면_성공() {
            // given
            MenuGroup menuGroup = new MenuGroup(1L, "주류");
            Menu menu = new Menu(1L, "맥주세트", Money.from(1000), menuGroup);

            List<MenuProduct> menuProducts = List.of(
                new MenuProduct(1L, 11, new Product(1L, "맥주", Money.from(100)), menu));

            // when
            menu.addMenuProducts(menuProducts);

            // when & then
            List<MenuProduct> actual = menu.getMenuProducts();
            assertThat(actual)
                .map(MenuProduct::getTotalPrice)
                .containsExactly(Money.from(1100));
        }

        @Test
        void 메뉴_상품의_가격_총합과_메뉴의_가격이_같아도_성공() {
            // given
            MenuGroup menuGroup = new MenuGroup(1L, "주류");
            Menu menu = new Menu(1L, "맥주세트", Money.from(1000), menuGroup);

            List<MenuProduct> menuProducts = List.of(
                new MenuProduct(1L, 10, new Product(1L, "맥주", Money.from(100)), menu));

            // when
            menu.addMenuProducts(menuProducts);

            // when & then
            List<MenuProduct> actual = menu.getMenuProducts();
            assertThat(actual)
                .map(MenuProduct::getTotalPrice)
                .containsExactly(Money.from(1000));
        }
    }
}
