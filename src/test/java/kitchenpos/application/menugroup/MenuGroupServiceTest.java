package kitchenpos.application.menugroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.LinkedList;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private static final Integer BASIC_MENU_GROUP_SIZE = 1;
    private static final Long BASIC_MENU_GROUP_ID = 1L;
    private static final String BASIC_MENU_GROUP_NAME = "신메뉴그룹";

    private List<MenuGroup> standardMenuGroups;
    private MenuGroup standardMenuGroup;

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        standardMenuGroup = new MenuGroup();
        standardMenuGroup.setId(BASIC_MENU_GROUP_ID);
        standardMenuGroup.setName(BASIC_MENU_GROUP_NAME);

        standardMenuGroups = new LinkedList<>();
        standardMenuGroups.add(standardMenuGroup);
    }

    @DisplayName("메뉴 그룹 서비스 조회한다.")
    @Test
    void getMenuGroups() {
        //given
        given(menuGroupDao.findAll()).willReturn(standardMenuGroups);

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups.size()).isEqualTo(BASIC_MENU_GROUP_SIZE);
    }

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void createMenuGroup() {
        //given
        given(menuGroupDao.save(any())).willReturn(standardMenuGroup);

        //when
        MenuGroup menuGroup = menuGroupService.create(standardMenuGroup);

        //then
        assertAll(
            () -> assertThat(menuGroup.getId()).isEqualTo(BASIC_MENU_GROUP_ID),
            () -> assertThat(menuGroup.getName()).isEqualTo(BASIC_MENU_GROUP_NAME)
        );

    }

}