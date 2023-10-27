package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.common.annotation.ServiceTest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.presentation.dto.request.CreateMenuGroupRequest;
import kitchenpos.support.TestSupporter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private TestSupporter testSupporter;

    @Nested
    class 메뉴_그룹_생성 {

        @Test
        void 메뉴_그룹을_생성한다() {
            // given
            final CreateMenuGroupRequest request = new CreateMenuGroupRequest("name");

            // when
            final MenuGroup menuGroup = menuGroupService.create(request);

            // then
            assertThat(menuGroup.getName()).isEqualTo(request.getName());
        }
    }

    @Nested
    class 메뉴_그룹_조회 {

        @Test
        void 메뉴_그룹에_대해_전체_조회한다() {
            // given
            final MenuGroup menuGroup = testSupporter.createMenuGroup();

            // when
            final List<MenuGroup> menuGroups = menuGroupService.list();

            // then
            assertThat(menuGroups.get(0)).usingRecursiveComparison()
                                         .isEqualTo(menuGroup);
        }
    }
}
