package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup(1L, "메뉴 그룹");
        given(menuGroupRepository.save(any(MenuGroup.class)))
            .willReturn(menuGroup);

        // when
        // then
        final MenuGroup result = menuGroupService.create(menuGroup);
        assertThat(result.getId()).isEqualTo(menuGroup.getId());
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void list() {
        // given
        final List<MenuGroup> expectedMenuGroups = List.of(
            new MenuGroup(1L, "메뉴 그룹 1"),
            new MenuGroup(2L, "메뉴 그룹 2"),
            new MenuGroup(3L, "메뉴 그룹 3"),
            new MenuGroup(4L, "메뉴 그룹 4")
        );
        given(menuGroupRepository.findAll())
            .willReturn(expectedMenuGroups);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups)
            .usingRecursiveComparison()
            .isEqualTo(expectedMenuGroups);
        assertThat(menuGroups.size()).isEqualTo(4);
    }
}
