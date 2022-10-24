package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends IntegrationServiceTest {


    @Nested
    class create_메서드는 {

        private final MenuGroup menuGroup = new MenuGroup("두 마리 메뉴");

        @Test
        void 메뉴그룹을_저장하고_ID값을_포함한것을_반환한다() {

            final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            assertThat(savedMenuGroup.getId()).isNotNull();
        }
    }

    @Nested
    class list_메서드는 {

        @Test
        void 메뉴그룹목록_반환한다 () {

            final List<MenuGroup> menuGroups = menuGroupService.list();

            assertThat(menuGroups).hasSize(4);
        }
    }
}