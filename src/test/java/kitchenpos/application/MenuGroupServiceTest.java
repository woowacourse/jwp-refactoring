package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.application.dto.menugroup.MenuGroupRequest;
import kitchenpos.application.dto.menugroup.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성, 저장한다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = createMenuGroup("menuGroup");
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menuGroup");
        given(menuGroupDao.save(any(MenuGroup.class)))
            .willReturn(menuGroup);

        // when
        final MenuGroupResponse result = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("모든 메뉴 그룹을 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroup menuGroup1 = createMenuGroup("menuGroup1");
        final MenuGroup menuGroup2 = createMenuGroup("menuGroup2");

        final List<MenuGroup> menuGroups = List.of(menuGroup1, menuGroup2);
        given(menuGroupDao.findAll())
            .willReturn(menuGroups);

        // when
        final List<MenuGroupResponse> foundMenuGroups = menuGroupService.list();

        // then
        assertThat(foundMenuGroups).usingRecursiveComparison()
            .isEqualTo(List.of(MenuGroupResponse.from(menuGroup1), MenuGroupResponse.from(menuGroup2)));
    }
}
