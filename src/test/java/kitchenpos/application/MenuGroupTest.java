package kitchenpos.application;

import static kitchenpos.support.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.application.dto.MenuGroupRequestDto;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest extends ServiceTest{

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        final MenuGroupRequestDto menuGroupRequestDto = 메뉴_그룹;

        final MenuGroup actual = menuGroupService.create(menuGroupRequestDto);

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹 리스트로 반환한다.")
    void list() {
        메뉴_그룹_등록(메뉴_그룹);
        메뉴_그룹_등록(메뉴_그룹);
        메뉴_그룹_등록(메뉴_그룹);

        final List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(3);
    }
}
