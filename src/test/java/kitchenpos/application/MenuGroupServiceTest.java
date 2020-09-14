package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    MenuGroupService menuGroupService;
    @Mock
    MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menu group");

        given(menuGroupDao.save(any())).willReturn(menuGroup);

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void list() {
        MenuGroup menuGroup1 = new MenuGroup();
        MenuGroup menuGroup2 = new MenuGroup();
        List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);

        given(menuGroupDao.findAll()).willReturn(menuGroups);

        List<MenuGroup> foundMenuGroups = menuGroupService.list();
        assertThat(foundMenuGroups.size()).isEqualTo(menuGroups.size());
    }
}
