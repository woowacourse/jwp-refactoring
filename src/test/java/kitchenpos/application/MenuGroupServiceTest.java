package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        this.menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createTest() {
        when(menuGroupDao.save(any())).thenReturn(MENU_GROUP1);

        assertAll(
                () -> assertThat(menuGroupService.create(MENU_GROUP1).getId()).isEqualTo(1L),
                () -> assertThat(menuGroupService.create(MENU_GROUP1).getName()).isEqualTo(
                        "두마리메뉴")
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회 할 수 있다.")
    @Test
    void listTest() {
        when(menuGroupService.list()).thenReturn(MENU_GROUPS);

        assertAll(
                () -> assertThat(menuGroupService.list().size()).isEqualTo(2),
                () -> assertThat(menuGroupService.list().get(0).getName()).isEqualTo(
                        "두마리메뉴"),
                () -> assertThat(menuGroupService.list().get(1).getName()).isEqualTo(
                        "한마리메뉴")
        );
    }
}