package kitchenpos.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
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
        Menu menu = Menu.of("TestMenu", BigDecimal.TEN, menuGroup);
        Product product = new Product();

        //when then
        Assertions.assertThatThrownBy(() -> MenuProduct.of(menu, quantity, product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 속해있는 상품은 null일 수 없다.")
    @Test
    void createMenuProductFailTest_ByQuantityIsNull() {
        //given
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        Menu menu = Menu.of("TestMenu", BigDecimal.TEN, menuGroup);
        Product product = new Product();

        //when then
        Assertions.assertThatThrownBy(() -> MenuProduct.of(menu, null, product))
                .isInstanceOf(NullPointerException.class);
    }
}
