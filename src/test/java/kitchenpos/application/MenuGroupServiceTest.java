package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MenuGroupServiceTest {

    @Nested
    class CreateMenuGroupTest extends ServiceTest {
        @Test
        void create_success() {
            final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("group1");
            final MenuGroup savedMenuGroup = menuGroupService.create(menuGroupRequest);

            assertThat(savedMenuGroup.getName()).isEqualTo("group1");
        }
    }

    @Nested
    class ListMenuGroupTest extends ServiceTest {
        @Test
        void list_success() {
            final MenuGroupRequest menuGroupRequest1 = new MenuGroupRequest("group1");
            menuGroupService.create(menuGroupRequest1);

            final MenuGroupRequest menuGroupRequest2 = new MenuGroupRequest("group1");
            menuGroupService.create(menuGroupRequest2);

            assertThat(menuGroupService.list()).hasSize(6);
        }
    }
}
