package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.InvalidMenuPriceException;
import kitchenpos.domain.exception.InvalidMenuProductException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductsTest {

    @Test
    void of_메서드는_유효한_데이터를_전달하면_menuProducts를_초기화한다() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1L);
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);

        // when
        final MenuProducts actual = MenuProducts.of(menu, List.of(menuProduct));

        // then
        assertAll(
                () -> assertThat(actual.getValues()).hasSize(1),
                () -> assertThat(actual.getValues().get(0).getMenu()).isEqualTo(menu)
        );
    }

    @ParameterizedTest(name = "menuProducts가 {0}이면 예외가 발생한다.")
    @NullAndEmptySource
    void of_메서드는_menuProducts가_비어_있으면_예외가_발생한다(final List<MenuProduct> invalidMenuProducts) {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product, 1L);
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);

        // when & then
        assertThatThrownBy(() -> MenuProducts.of(menu, invalidMenuProducts))
                .isInstanceOf(InvalidMenuProductException.class);
    }

    @Test
    void of_메서드는_menuProducts의_가격보다_menu의_가격이_클_경우_예외가_발생한다() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct validMenuProduct = new MenuProduct(product, 5L);
        final MenuProduct invalidMenuProduct = new MenuProduct(product, 1L);
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Menu menu = Menu.of("메뉴", new BigDecimal("30"), List.of(validMenuProduct), menuGroup);

        // when & then
        assertThatThrownBy(() -> MenuProducts.of(menu, List.of(invalidMenuProduct)))
                .isInstanceOf(InvalidMenuPriceException.class);
    }
}
