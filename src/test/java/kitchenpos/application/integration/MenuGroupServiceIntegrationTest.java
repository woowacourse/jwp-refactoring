package kitchenpos.application.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.common.factory.MenuGroupFactory;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuGroupServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("생성 - 메뉴 그룹을 생성(등록)할 수 있다.")
    @Test
    void create_success() {
        // given
        MenuGroup menuGroup = MenuGroupFactory.create("두마리메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("조회 - 전체 메뉴 그룹을 조회할 수 있다.")
    @Test
    void list_success() {
        // given
        MenuGroup menuGroup1 = MenuGroupFactory.create("두마리메뉴");
        MenuGroup menuGroup2 = MenuGroupFactory.create("한마리메뉴");

        // when
        MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);
        MenuGroup savedMenuGroup2 = menuGroupService.create(menuGroup2);
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
        assertThat(menuGroups)
            .containsExactlyInAnyOrder(savedMenuGroup1, savedMenuGroup2);
    }
}
