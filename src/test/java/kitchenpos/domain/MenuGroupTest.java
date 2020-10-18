package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupTest {

    @DisplayName("MenuGroup 생성 실패 - 빈 문자열")
    @Test
    public void createFailMenuGroup() {
        assertThatThrownBy(() -> new MenuGroup(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("MenuGroup 생성")
    @Test
    public void createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup("패스트 푸드");

        assertThat(menuGroup.getName()).isEqualTo("패스트 푸드");
    }

}