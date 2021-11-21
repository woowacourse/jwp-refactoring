package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dtos.MenuGroupRequest;
import kitchenpos.application.dtos.MenuGroupResponse;
import kitchenpos.application.dtos.MenuGroupResponses;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다")
    @Test
    void create() {
        final MenuGroupRequest request = new MenuGroupRequest("하노이 아침메뉴 세트");
        final MenuGroup savedMenuGroup = MenuGroup.builder()
                .name(request.getName())
                .id(1L)
                .build();
        new MenuGroup();

        when(menuGroupRepository.save(any())).thenReturn(savedMenuGroup);

        final MenuGroupResponse actual = menuGroupService.create(request);
        assertThat(actual.getId()).isEqualTo(1L);
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getName()).isEqualTo(request.getName())
        );
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다")
    @Test
    void list() {
        final MenuGroup menuGroup1 = new MenuGroup();
        final MenuGroup menuGroup2 = new MenuGroup();
        final List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);

        when(menuGroupRepository.findAll()).thenReturn(menuGroups);

        final MenuGroupResponses actual = menuGroupService.list();
        assertThat(actual.getMenuGroupResponses()).usingRecursiveComparison()
                .isEqualTo(Arrays.asList(menuGroup1, menuGroup2));
    }
}
