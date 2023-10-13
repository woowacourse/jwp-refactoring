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

    @DisplayName("메뉴 그룹을 생성, 저장한다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup");
        given(menuGroupDao.save(any(MenuGroup.class)))
            .willReturn(menuGroup);

        // when
        final MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("menuGroup");
    }

    @DisplayName("모든 메뉴 그룹을 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroup menuGroup1 = new MenuGroup();
        final MenuGroup menuGroup2 = new MenuGroup();
        menuGroup1.setId(1L);
        menuGroup1.setName("menuGroup1");
        menuGroup2.setId(2L);
        menuGroup2.setName("menuGroup2");

        final List<MenuGroup> menuGroups = List.of(menuGroup1, menuGroup2);
        given(menuGroupDao.findAll())
            .willReturn(menuGroups);

        // when
        final List<MenuGroup> foundMenuGroups = menuGroupService.list();

        // then
        assertThat(foundMenuGroups).usingRecursiveComparison()
            .isEqualTo(menuGroups);
    }
}
