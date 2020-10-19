package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.TestFixture;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest extends TestFixture {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("MenuGroup 생성 테스트")
    @Test
    void createTest() {
        given(menuGroupDao.save(any())).willReturn(MENU_GROUP_1);

        MenuGroup persistedMenuGroup = menuGroupService.create(MENU_GROUP_1);

        assertThat(persistedMenuGroup).isEqualTo(MENU_GROUP_1);
    }

    @DisplayName("MenuGroup 조회 테스트")
    @Test
    void listTest() {
        List<MenuGroup> menuGroups = Arrays.asList(MENU_GROUP_1, MENU_GROUP_2);
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        List<MenuGroup> foundMenuGroups = menuGroupService.list();

        assertAll(
            () -> assertThat(foundMenuGroups).hasSize(2),
            () -> assertThat(foundMenuGroups.get(0)).usingRecursiveComparison().isEqualTo(MENU_GROUP_1),
            () -> assertThat(foundMenuGroups.get(1)).usingRecursiveComparison().isEqualTo(MENU_GROUP_2)
        );
    }
}