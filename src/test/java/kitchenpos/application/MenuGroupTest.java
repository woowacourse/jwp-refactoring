package kitchenpos.application;

import static kitchenpos.support.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest extends ServiceTest{

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        final MenuGroup menuGroup = 메뉴_그룹;

        final MenuGroup actual = menuGroupService.create(menuGroup);

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹 리스트로 반환한다.")
    void list() {
        메뉴_그룹_등록(메뉴_그룹);
        메뉴_그룹_등록(메뉴_그룹);
        메뉴_그룹_등록(메뉴_그룹);

        final List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(3);
    }
}
