package kitchenpos.application.integration;

import kitchenpos.menugroups.dto.CreateMenuGroupRequest;
import kitchenpos.menugroups.dto.ListMenuGroupResponse;
import kitchenpos.menugroups.dto.MenuGroupResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ApplicationIntegrationTest {

    @Test
    void create_menu_group() {
        //given
        final CreateMenuGroupRequest menuGroup = CreateMenuGroupRequest.of("치킨");
        //when
        final MenuGroupResponse createdMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(createdMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void list_menu_groups() {
        //given
        final int originalSize = menuGroupService.list().getMenuGroups().size();
        final CreateMenuGroupRequest menuGroup = CreateMenuGroupRequest.of("치킨");
        final MenuGroupResponse createdMenuGroup = menuGroupService.create(menuGroup);

        //when
        final ListMenuGroupResponse foundMenuGroups = menuGroupService.list();

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(foundMenuGroups.getMenuGroups()).hasSize(originalSize + 1);
            softly.assertThat(foundMenuGroups.getMenuGroups().get(0))
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(createdMenuGroup);
        });
    }
}