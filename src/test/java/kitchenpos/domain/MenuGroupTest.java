package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuGroupTest {

    @DisplayName("메뉴 그룹 생성")
    @Test
    void createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup("탄산음료");

        assertAll(
            () -> assertThat(menuGroup).isNotNull(),
            () -> assertThat(menuGroup.getName()).isEqualTo("탄산음료")
        );

    }

    @DisplayName("메뉴 그룹 생성 실패 - 메뉴 그룹 이름 없음")
    @ParameterizedTest
    @NullAndEmptySource
    void createFail_Name_IsEmpty(String name) {
        assertThatThrownBy(() -> new MenuGroup(name))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴그룹 이름을 입력해주세요");
    }
}
