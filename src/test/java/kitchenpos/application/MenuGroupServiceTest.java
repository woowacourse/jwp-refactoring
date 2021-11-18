package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MenuGroupServiceTest extends ServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        Long idToSave = 1L;
        when(menuGroupRepository.save(any(MenuGroup.class))).thenAnswer(invocation -> {
            MenuGroup menuGroup = invocation.getArgument(0);
            return new MenuGroup(idToSave, menuGroup.getName());
        });

        MenuGroupRequest request = new MenuGroupRequest("추천메뉴");
        MenuGroupResponse actual = menuGroupService.create(request);
        MenuGroupResponse expected = MenuGroupResponse.from(
            new MenuGroup(idToSave, request.getName())
        );

        verify(menuGroupRepository, times(1)).save(any(MenuGroup.class));
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        List<MenuGroup> menuGroups = Arrays.asList(
            new MenuGroup(1L, "두마리메뉴"),
            new MenuGroup(2L, "한마리메뉴"),
            new MenuGroup(3L, "순살파닭두마리메뉴")
        );
        when(menuGroupRepository.findAll()).thenReturn(menuGroups);

        List<MenuGroupResponse> actual = menuGroupService.list();
        List<MenuGroupResponse> expected = MenuGroupResponse.listFrom(menuGroups);

        verify(menuGroupRepository, times(1)).findAll();
        assertThat(actual).hasSameSizeAs(expected)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(expected);
    }
}
