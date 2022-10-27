package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @DisplayName("메뉴 그룹을 만들 때 이름이 없으면 예외가 발생한다.")
    @Test
    void createFailureWhenNameIsEmpty() {
        assertThatThrownBy(
                () -> new MenuGroup(null)
        ).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 공백일 수 없습니다.");
    }

}
