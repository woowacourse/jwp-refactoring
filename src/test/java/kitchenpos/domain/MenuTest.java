package kitchenpos.domain;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.prodcut.Price;
import kitchenpos.prodcut.Product;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_가격이_메뉴_상품의_총_가격보다_큰_경우_예외가_발생한다() {
        // given
        Menu menu = new Menu(
                "한마리 메뉴",
                new Price(11000),
                new MenuGroup("치킨메뉴")
        );
        MenuProduct menuProduct = new MenuProduct(
                menu,
                new Product("치킨", new Price(5000)),
                2L
        );

        // when, then
        assertThatThrownBy(() -> menu.addMenuProducts(List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
