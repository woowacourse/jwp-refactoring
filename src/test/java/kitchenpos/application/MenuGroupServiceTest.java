package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.LinkedList;
import java.util.List;
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

    private List<MenuGroup> standardMenuGroups;
    private MenuGroup standardMenuGroup;

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        standardMenuGroup = new MenuGroup();
        standardMenuGroup.setId(1L);
        standardMenuGroup.setName("신메뉴그룹");

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
        assertThat(menuGroups.size()).isEqualTo(1);
    }

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void createMenuGroup() {
        //given
        given(menuGroupDao.save(any())).willReturn(standardMenuGroup);

        //when
        MenuGroup menuGroup = menuGroupDao.save(standardMenuGroup);

        //then
        assertAll(
            () -> assertThat(standardMenuGroup.getId()).isEqualTo(1L),
            () -> assertThat(standardMenuGroup.getName()).isEqualTo("신메뉴그룹")
        );

    }

}