package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    public void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("Test Menu Group");
    }

    @Test
    public void createMenuGroupTest() {
        //given
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(menuGroup);

        //when
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(menuGroup.getId()).isEqualTo(createdMenuGroup.getId());
        assertThat(menuGroup.getName()).isEqualTo(createdMenuGroup.getName());
    }

    @Test
    public void listMenuGroupTest() {
        //given
        List<MenuGroup> menuGroups = Collections.singletonList(menuGroup);
        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        //when
        List<MenuGroup> returnedMenuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups.size()).isEqualTo(returnedMenuGroups.size());
        assertThat(menuGroups.get(0).getId()).isEqualTo(returnedMenuGroups.get(0).getId());
        assertThat(menuGroups.get(0).getName()).isEqualTo(returnedMenuGroups.get(0).getName());
    }
}
