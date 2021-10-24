package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("그룹1");
        menuGroup.setId(1L);

        when(menuGroupDao.save(any(MenuGroup.class)))
                .thenReturn(menuGroup);

        MenuGroup actual = menuGroupService.create(menuGroup);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("모든 메뉴그룹을 조회한다.")
    void list() {
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("그룹1");
        menuGroup1.setId(1L);

        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("그룹2");
        menuGroup2.setId(2L);

        List<MenuGroup> menuGroups = new ArrayList<>();
        menuGroups.add(menuGroup1);
        menuGroups.add(menuGroup2);

        when(menuGroupDao.findAll())
                .thenReturn(menuGroups);

        assertThat(menuGroupService.list()).hasSize(2);
    }
}
