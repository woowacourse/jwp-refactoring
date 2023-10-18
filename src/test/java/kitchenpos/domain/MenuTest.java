package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @Test
    @DisplayName("메뉴 금액이 null인 경우, 생성할 수 없다.")
    void createMenuFailTest_ByPriceIsNull() {
        //given
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        //when then
        assertThatThrownBy(() -> Menu.createWithEmptyMenuProducts("TestMenu", null, menuGroup))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest(name = "메뉴 금액이 0원 미만인 경우, 생성할 수 없다")
    @ValueSource(ints = {-100, -1})
    void createMenuFailTest_ByPriceIsLessThanZero(int price) {
        //given
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        BigDecimal menuPrice = BigDecimal.valueOf(price);

        //when then
        assertThatThrownBy(() -> Menu.createWithEmptyMenuProducts("TestMenu", menuPrice, menuGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 금액은 0원 이상이어야 합니다.");
    }

    @DisplayName("메뉴 그룹이 null일 경우, 생성할 수 없다.")
    @Test
    void createMenuFailTest_ByMenuGroupIsNull() {
        //given
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        //when then
        assertThatThrownBy(() -> Menu.createWithEmptyMenuProducts("TestMenu", menuPrice, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("메뉴 이름이 null인 경우, 생성할 수 없다.")
    void createMenuFailTest_ByNameIsNull() {
        //given
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        //when then
        assertThatThrownBy(() -> Menu.createWithEmptyMenuProducts(null, menuPrice, menuGroup))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("메뉴 이름이 1글자 이하인 경우, 생성할 수 없다.")
    void createMenuFailTest_ByNameLengthIsLessThanOne() {
        //given
        String menuName = "";
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        //when then
        assertThatThrownBy(() -> Menu.createWithEmptyMenuProducts(menuName, menuPrice, menuGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 이름의 길이는 1글자 이상, 255글자 이하여야 합니다.");
    }

    @Test
    @DisplayName("메뉴 이름이 255글자 이상인 경우, 생성할 수 없다.")
    void createMenuFailTest_ByNameLengthIsMoreThan255() {
        //given
        String menuName = "a".repeat(256);
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        //when then
        assertThatThrownBy(() -> Menu.createWithEmptyMenuProducts(menuName, menuPrice, menuGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 이름의 길이는 1글자 이상, 255글자 이하여야 합니다.");
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void createMenuSuccessTest() {
        //given
        String menuName = "TestMenuName";
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        //when then
        assertDoesNotThrow(() -> Menu.createWithEmptyMenuProducts(menuName, menuPrice, menuGroup));
    }

}
