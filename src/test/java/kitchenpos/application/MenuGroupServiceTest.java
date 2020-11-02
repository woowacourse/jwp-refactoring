package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createTest() {
        when(menuGroupDao.save(any())).thenReturn(MENU_GROUP1);

        MenuGroup menuGroup = menuGroupService.create(MENU_GROUP1);
        assertAll(
                () -> assertThat(menuGroup.getId()).isEqualTo(1L),
                () -> assertThat(menuGroup.getName()).isEqualTo(
                        "두마리메뉴")
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회 할 수 있다.")
    @Test
    void listTest() {
        when(menuGroupDao.findAll()).thenReturn(MENU_GROUPS);

        List<MenuGroup> list = menuGroupService.list();
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(list.get(0).getName()).isEqualTo("두마리메뉴"),
                () -> assertThat(list.get(1).getName()).isEqualTo("한마리메뉴")
        );
    }
}