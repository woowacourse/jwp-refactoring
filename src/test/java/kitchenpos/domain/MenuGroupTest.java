package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.exception.MenuGroupException.InvalidMenuGroupNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴그룹을 생성할 때 이름이 null이면 예외가 발생한다.")
    void init_fail1() {
        assertThatThrownBy(() -> MenuGroup.from(null))
                .isInstanceOf(InvalidMenuGroupNameException.class);
    }

    @Test
    @DisplayName("메뉴그룹을 생성할 때 이름이 빈 문자열이면 예외가 발생한다.")
    void init_fail2() {
        assertThatThrownBy(() -> MenuGroup.from(""))
                .isInstanceOf(InvalidMenuGroupNameException.class);
    }
}
