package kitchenpos.application.integration;

import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ApplicationIntegrationTest {

    @Test
    void create_menu_group() {
        //given
        final MenuGroup menuGroup = new MenuGroup("치킨");

        //when
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(createdMenuGroup)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    void list_menu_groups() {
        //given
        final int originalSize = menuGroupService.list().size();
        final MenuGroup menuGroup = new MenuGroup("치킨");
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        //when
        final List<MenuGroup> foundMenuGroups = menuGroupService.list();

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(foundMenuGroups).hasSize(originalSize + 1);
            softly.assertThat(foundMenuGroups)
                    .usingRecursiveFieldByFieldElementComparator()
                    .contains(createdMenuGroup);
        });
    }
}