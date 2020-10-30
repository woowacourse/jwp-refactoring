package kitchenpos.application;

import static kitchenpos.MenuGroupFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("id가 없는 메뉴 그룹으로 id가 있는 메뉴 그룹을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final MenuGroup expectedMenuGroup = createMenuGroupWithId(1L);
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(createMenuGroupWithId(1L));
        final MenuGroup persistMenuGroup = menuGroupService.create(createMenuGroupWithoutId());

        assertThat(expectedMenuGroup).isEqualToComparingFieldByField(persistMenuGroup);
    }

    @DisplayName("메뉴 그룹들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final List<MenuGroup> expectedMenuGroups = createMenuGroups();
        given(menuGroupDao.findAll()).willReturn(createMenuGroups());
        final List<MenuGroup> persistMenuGroups = menuGroupService.list();

        assertThat(expectedMenuGroups).usingRecursiveComparison().isEqualTo(persistMenuGroups);
    }
}
