package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuProductTest {

    @ParameterizedTest(name = "메뉴에 속해있는 상품의 개수는 1개 미만일 수 없다.")
    @ValueSource(longs = {-100, 0})
    void createMenuProductFailTest_ByQuantityIsLessThanOne(Long quantity) {
        //given
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        Menu menu = Menu.createWithEmptyMenuProducts("TestMenu", BigDecimal.TEN, menuGroup);
        Product product = Product.create("TestProduct", BigDecimal.TEN);

        //when then
        assertThatThrownBy(() -> MenuProduct.create(menu, quantity, product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 개수는 1개 이상이어야 합니다.");
    }

    @DisplayName("메뉴에 속해있는 상품 수량은 null일 수 없다.")
    @Test
    void createMenuProductFailTest_ByQuantityIsNull() {
        //given
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        Menu menu = Menu.createWithEmptyMenuProducts("TestMenu", BigDecimal.TEN, menuGroup);
        Product product = Product.create("TestProduct", BigDecimal.TEN);

        //when then
        assertThatThrownBy(() -> MenuProduct.create(menu, null, product))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("상품 수량은 null일 수 없습니다.");
    }
}
