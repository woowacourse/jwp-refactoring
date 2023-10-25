package kitchenpos.application;

import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupServiceTest extends ServiceBaseTest {

    @Autowired
    protected MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create() {
        //given
        final MenuGroupRequest request = new MenuGroupRequest("오션 메뉴 그룹");

        //when
        final MenuGroupResponse response = menuGroupService.create(request);

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(request.getName())
        );
    }

    @Test
    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    void list() {
        //given
        final MenuGroup menuGroup1 = menuGroupRepository.save(new MenuGroup("오션 메뉴 그룹"));
        final MenuGroup menuGroup2 = menuGroupRepository.save(new MenuGroup("동해 메뉴 그룹"));

        //when
        final List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        //then
        assertAll(
                () -> assertThat(menuGroupResponses).hasSize(2),
                () -> assertThat(menuGroupResponses.get(0).getName()).isEqualTo(menuGroup1.getName()),
                () -> assertThat(menuGroupResponses.get(1).getName()).isEqualTo(menuGroup2.getName())
        );
    }
}
