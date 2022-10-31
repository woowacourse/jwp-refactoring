package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuGroupTest {

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
    void menuGroup() {
        assertDoesNotThrow(() -> new MenuGroup("name"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("name이 비어있을 경우 예외가 발생한다.")
    void emptyName(String name) {
        assertThatThrownBy(() -> new MenuGroup(name))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴그룹의 이름은 비어있을 수 없습니다.");
    }

}
