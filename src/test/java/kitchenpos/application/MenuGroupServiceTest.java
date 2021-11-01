package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.MenuFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = createMenuGroup();
        when(menuGroupDao.save(menuGroup)).thenReturn(createMenuGroup(1L));

        MenuGroup actual = menuGroupService.create(menuGroup);

        assertDoesNotThrow(() -> menuGroupService.create(menuGroup));
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup1 = createMenuGroup();
        MenuGroup menuGroup2 = createMenuGroup();
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroup> actual = menuGroupService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(menuGroup1, menuGroup2)
        );
    }
}
