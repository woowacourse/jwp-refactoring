package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupName;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("menuGroup");
        final MenuGroupName menuGroupName = new MenuGroupName("menuGroup");
        final MenuGroup menuGroup = new MenuGroup(menuGroupName);
        given(menuGroupRepository.save(any())).willReturn(menuGroup);

        // when
        final MenuGroup result = menuGroupService.create(request);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        final MenuGroupName menuGroupName1 = new MenuGroupName("group1");
        final MenuGroupName menuGroupName2 = new MenuGroupName("group2");
        final List<MenuGroup> menuGroups = List.of(new MenuGroup(menuGroupName1), new MenuGroup(menuGroupName2));
        given(menuGroupRepository.findAll()).willReturn(menuGroups);

        // when
        final List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(menuGroups);
    }
}
