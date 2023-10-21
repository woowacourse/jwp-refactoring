package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.IntegrationTest;
import kitchenpos.application.dto.MenuGroupCreationRequest;
import kitchenpos.application.dto.result.MenuGroupResult;
import kitchenpos.application.menu.MenuGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        final MenuGroupCreationRequest request = new MenuGroupCreationRequest("Chicken-group");

        // when
        final MenuGroupResult createdMenuGroup = menuGroupService.create(request);

        // then
        assertThat(createdMenuGroup.getId()).isNotNull();
    }

    @Test
    void list() {
        // given
        generateMenuGroup("Chicken-group");
        generateMenuGroup("Pizza-group");

        // when
        final List<MenuGroupResult> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }
}
