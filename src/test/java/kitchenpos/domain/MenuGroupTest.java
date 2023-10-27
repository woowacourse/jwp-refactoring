package kitchenpos.domain;

import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuGroupTest {

    @DisplayName("메뉴 그룹 생성 시, 이름이 비어있으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "  "})
    void menuGroup_FailWithBlankName(String blankName) {
        // when & then
        assertThatThrownBy(() -> MenuGroup.create(blankName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹의 이름이 비어있습니다.");
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void menuGroup() {
        // then
        assertDoesNotThrow(() -> MenuGroup.create("메뉴 그룹명"));
    }
}
