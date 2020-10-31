package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

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
        MenuGroup menuGroup = TestObjectUtils.createMenuGroup(1L, "두마리메뉴");

        when(menuGroupDao.save(any())).thenReturn(menuGroup);

        assertAll(
                () -> assertThat(menuGroupService.create(menuGroup).getId()).isEqualTo(1L),
                () -> assertThat(menuGroupService.create(menuGroup).getName()).isEqualTo(
                        "두마리메뉴")
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회 할 수 있다.")
    @Test
    void listTest() {
        MenuGroup menuGroup1 = TestObjectUtils.createMenuGroup(1L, "두마리메뉴");
        MenuGroup menuGroup2 = TestObjectUtils.createMenuGroup(2L, "한마리메뉴");
        List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);

        when(menuGroupService.list()).thenReturn(menuGroups);

        assertAll(
                () -> assertThat(menuGroupService.list().size()).isEqualTo(2),
                () -> assertThat(menuGroupService.list().get(0).getName()).isEqualTo(
                        "두마리메뉴"),
                () -> assertThat(menuGroupService.list().get(1).getName()).isEqualTo(
                        "한마리메뉴")
        );
    }
}