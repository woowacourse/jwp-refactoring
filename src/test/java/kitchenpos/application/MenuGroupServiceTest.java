package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
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

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("메뉴 그룹");
        given(menuGroupDao.save(any(MenuGroup.class)))
            .willReturn(menuGroup);

        // when
        // then
        final MenuGroup result = menuGroupService.create(menuGroup);
        assertThat(result.getId()).isEqualTo(menuGroup.getId());
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void func() {
        // given
        final List<MenuGroup> expectedMenuGroups = List.of(
            new MenuGroup(),
            new MenuGroup(),
            new MenuGroup(),
            new MenuGroup()
        );
        given(menuGroupDao.findAll())
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
