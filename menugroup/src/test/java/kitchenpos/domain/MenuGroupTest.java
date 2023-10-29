package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuGroupTest {

    @Test
    void 생성시_이름이_없으면_예외가_발생한다() {
        //given
        String 이름 = null;

        //expect
        assertThatThrownBy(() -> new MenuGroup(이름))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹의 이름은 필수로 입력해야 합니다.");
    }

    @Test
    void id가_같으면_동등하다() {
        //given
        MenuGroup 메뉴그룹 = new MenuGroup(1L, "메뉴그룹");

        //when
        boolean actual = 메뉴그룹.equals(new MenuGroup(1L, "다른메뉴그룹"));

        //then
        assertThat(actual).isTrue();
    }

}
