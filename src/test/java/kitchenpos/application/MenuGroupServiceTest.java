package kitchenpos.application;

import static kitchenpos.support.fixture.MenuGroupFixture.간장_양념_세_마리_메뉴;
import static kitchenpos.support.fixture.MenuGroupFixture.단짜_두_마리_메뉴;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.request.MenuGroupRequest;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

    @Nested
    class create_메서드는 extends IntegrationServiceTest {

        private final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("두 마리 메뉴");

        @Test
        void 메뉴그룹을_저장하고_ID값을_포함한것을_반환한다() {

            final MenuGroup savedMenuGroup = menuGroupService.create(menuGroupRequest);

            assertThat(savedMenuGroup.getId()).isNotNull();
        }
    }

    @Nested
    class list_메서드는 extends IntegrationServiceTest {

        @BeforeEach
        void setUp() {
            menuGroupRepository.save(단짜_두_마리_메뉴);
            menuGroupRepository.save(간장_양념_세_마리_메뉴);
        }

        @Test
        void 메뉴그룹목록_반환한다() {

            final List<MenuGroup> menuGroups = menuGroupService.list();

            assertThat(menuGroups).hasSize(2);
        }
    }
}
