package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("새로운 메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("치킨");
        final MenuGroup menuGroup = new MenuGroup(10L, "치킨");

        given(menuGroupRepository.save(any()))
                .willReturn(menuGroup);

        // when & then
        assertThat(menuGroupService.create(menuGroupRequest)).isEqualTo(menuGroup.getId());
        then(menuGroupRepository).should(times(1)).save(any());
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final MenuGroup menuGroup1 = new MenuGroup(10L, "치킨");
        final MenuGroup menuGroup2 = new MenuGroup(11L, "피자");

        final List<MenuGroup> menuGroups = List.of(menuGroup1, menuGroup2);
        final List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toUnmodifiableList());

        given(menuGroupRepository.findAll())
                .willReturn(menuGroups);

        // when & then
        assertThat(menuGroupService.list()).usingRecursiveComparison().isEqualTo(menuGroupResponses);
        then(menuGroupRepository).should(times(1)).findAll();
    }
}
