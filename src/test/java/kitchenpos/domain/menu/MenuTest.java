package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.exception.InvalidMenuPriceException;
import kitchenpos.domain.exception.InvalidMenuProductException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void of_메서드는_유효한_값을_전달하면_Menu를_초기화한다() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");

        // when & then
        assertThatCode(() -> Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void of_메서드는_메뉴의_가격보다_메뉴_상품의_총_가격이_작으면_예외가_발생한다() {
        // given
        final Product product = new Product("상품", BigDecimal.ONE);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), product.price(), product.name(), 1L);
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");

        // when & then
        assertThatThrownBy(() -> Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup.getId()))
                .isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    void of_메서드는_메뉴_상품이_비어_있다면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");

        // when & then
        assertThatThrownBy(() -> Menu.of("메뉴", BigDecimal.TEN, Collections.emptyList(), menuGroup.getId()))
                .isInstanceOf(InvalidMenuProductException.class);
    }
}
