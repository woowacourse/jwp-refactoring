package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.menugroup.MenuGroupRequest;
import kitchenpos.application.dto.menugroup.MenuGroupResponse;
import kitchenpos.support.DataDependentIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends DataDependentIntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성, 저장한다.")
    @Test
    void create() {
        // given
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menuGroup");

        // when
        final MenuGroupResponse result = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(result.getName()).isEqualTo("menuGroup");
        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("모든 메뉴 그룹을 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroupRequest menuGroupRequest1 = new MenuGroupRequest("menuGroup1");
        final MenuGroupRequest menuGroupRequest2 = new MenuGroupRequest("menuGroup2");
        final MenuGroupResponse menuGroupResponse1 = menuGroupService.create(menuGroupRequest1);
        final MenuGroupResponse menuGroupResponse2 = menuGroupService.create(menuGroupRequest2);

        // when
        final List<MenuGroupResponse> foundMenuGroups = menuGroupService.list();

        // then
        assertThat(foundMenuGroups).hasSize(2);
        assertThat(foundMenuGroups).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(List.of(menuGroupResponse1, menuGroupResponse2));
    }
}
