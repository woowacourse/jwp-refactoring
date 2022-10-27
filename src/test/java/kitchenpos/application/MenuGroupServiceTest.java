package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MenuGroupServiceTest {

    @Nested
    class CreateMenuGroupTest extends ServiceTest {
        @Test
        void create_success() {
            final MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("group1");
            final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            assertThat(savedMenuGroup.getName()).isEqualTo("group1");
        }
    }

    @Nested
    class ListMenuGroupTest extends ServiceTest {
        @Test
        void list_success() {
            final MenuGroup menuGroup1 = new MenuGroup();
            menuGroup1.setName("group1");
            menuGroupService.create(menuGroup1);

            final MenuGroup menuGroup2 = new MenuGroup();
            menuGroup2.setName("group1");
            menuGroupService.create(menuGroup1);

            assertThat(menuGroupService.list()).hasSize(6);
        }
    }
}
