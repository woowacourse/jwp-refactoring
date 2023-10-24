package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuTest {


    @Mock
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴 금액이 null인 경우, 생성할 수 없다.")
    void createMenuFailTest_ByPriceIsNull() {
        //given
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        MenuProducts menuProducts = MenuProducts.from(List.of(MenuProduct.create(1L, 1L)));

        //when then
        assertThatThrownBy(() -> Menu.create("TestMenu", null, menuGroup, menuProducts, menuValidator))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("메뉴 금액은 null일 수 없습니다.");
    }

    @ParameterizedTest(name = "메뉴 금액이 0원 미만인 경우, 생성할 수 없다")
    @ValueSource(ints = {-100, -1})
    void createMenuFailTest_ByPriceIsLessThanZero(int price) {
        //given
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        BigDecimal menuPrice = BigDecimal.valueOf(price);
        MenuProducts menuProducts = MenuProducts.from(List.of(MenuProduct.create(1L, 1L)));

        //when then
        assertThatThrownBy(() -> Menu.create("TestMenu", menuPrice, menuGroup, menuProducts, menuValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 금액은 0원 이상이어야 합니다.");
    }

    @DisplayName("메뉴 그룹이 null일 경우, 생성할 수 없다.")
    @Test
    void createMenuFailTest_ByMenuGroupIsNull() {
        //given
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuProducts menuProducts = MenuProducts.from(List.of(MenuProduct.create(1L, 1L)));

        //when then
        assertThatThrownBy(() -> Menu.create("TestMenu", menuPrice, null, menuProducts, menuValidator))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("메뉴 그룹은 null일 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴 이름이 null인 경우, 생성할 수 없다.")
    void createMenuFailTest_ByNameIsNull() {
        //given
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        MenuProducts menuProducts = MenuProducts.from(List.of(MenuProduct.create(1L, 1L)));

        //when then
        assertThatThrownBy(() -> Menu.create(null, menuPrice, menuGroup, menuProducts, menuValidator))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("메뉴 이름은 null일 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴 이름이 1글자 이하인 경우, 생성할 수 없다.")
    void createMenuFailTest_ByNameLengthIsLessThanOne() {
        //given
        String menuName = "";
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");
        MenuProducts menuProducts = MenuProducts.from(List.of(MenuProduct.create(1L, 1L)));

        //when then
        assertThatThrownBy(() -> Menu.create(menuName, menuPrice, menuGroup, menuProducts, menuValidator))
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
        MenuProducts menuProducts = MenuProducts.from(List.of(MenuProduct.create(1L, 1L)));

        //when then
        assertThatThrownBy(() -> Menu.create(menuName, menuPrice, menuGroup, menuProducts, menuValidator))
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
        MenuProducts menuProducts = MenuProducts.from(List.of(MenuProduct.create(1L, 1L)));

        //when then
        assertDoesNotThrow(() -> Menu.create(menuName, menuPrice, menuGroup, menuProducts, menuValidator));
    }

}
