package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @DisplayName("메뉴 그룹 생성 테스트")
    public void createMenuGroupTest() {
        //given
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(menuGroup);

        //when
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(createdMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회 테스트")
    public void listMenuGroupTest() {
        //given
        List<MenuGroup> menuGroups = List.of(menuGroup);
        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        //when
        List<MenuGroup> returnedMenuGroups = menuGroupService.list();

        //then
        assertThat(returnedMenuGroups).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(menuGroups);
    }
}
