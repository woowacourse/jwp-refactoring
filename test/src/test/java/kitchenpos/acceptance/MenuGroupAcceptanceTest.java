package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        // when
        final MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupCreateRequest("치킨"));

        // then
        assertThat(menuGroup.getId()).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo("치킨");
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroupResponse menuGroup1 = menuGroupService.create(new MenuGroupCreateRequest("치킨"));
        final MenuGroupResponse menuGroup2 = menuGroupService.create(new MenuGroupCreateRequest("사이드메뉴"));

        // when
        final List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups.size()).isEqualTo(2);
        assertThat(menuGroups).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(List.of(menuGroup1, menuGroup2));
    }
}
