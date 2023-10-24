package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.common.annotation.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.CreateMenuGroupRequest;
import kitchenpos.support.TestSupporter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private TestSupporter testSupporter;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        final CreateMenuGroupRequest request = new CreateMenuGroupRequest("name");

        // when
        final MenuGroup menuGroup = menuGroupService.create(request);

        // then
        assertThat(menuGroup.getName()).isEqualTo(request.getName());
    }

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
