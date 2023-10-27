package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹 이름이 null인 경우, 생성할 수 없다.")
    void createMenuGroupFailTest_ByNameIsNull() {
        //when then
        assertThatThrownBy(() -> MenuGroup.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹 이름은 1글자 이상, 255자 이하여야 합니다.");
    }

    @Test
    @DisplayName("메뉴 그룹 이름이 1글자 이하인 경우, 생성할 수 없다.")
    void createMenuGroupFailTest_ByNameLengthIsLessThanOne() {
        //when then
        assertThatThrownBy(() -> MenuGroup.create(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹 이름은 1글자 이상, 255자 이하여야 합니다.");
    }

    @Test
    @DisplayName("메뉴 그룹 이름이 255글자 이상인 경우, 생성할 수 없다.")
    void createMenuGroupFailTest_ByNameLengthIsMoreThan255() {
        //given
        String menuGroupName = "a".repeat(256);

        //when then
        assertThatThrownBy(() -> MenuGroup.create(menuGroupName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹 이름은 1글자 이상, 255자 이하여야 합니다.");
    }

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void createMenuGroupSuccessTest() {
        //given
        String menuGroupName = "TestMenuGroupName";

        //when then
        assertDoesNotThrow(() -> MenuGroup.create(menuGroupName));
    }

}
