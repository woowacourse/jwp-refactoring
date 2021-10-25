package kitchenpos.application;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다")
    @Test
    void create() {
        final MenuGroup menuGroup = MenuGroup.builder()
                .name("하노이 아침메뉴 셋트")
                .build();
        final MenuGroup savedMenuGroup = MenuGroup.builder()
                .of(menuGroup)
                .id(1L)
                .build();
                new MenuGroup();

        when(menuGroupDao.save(menuGroup)).thenReturn(savedMenuGroup);

        final MenuGroup actual = menuGroupService.create(menuGroup);
        assertThat(actual.getId()).isEqualTo(1L);
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getName()).isEqualTo("하노이 아침메뉴 셋트")
        );
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다")
    @Test
    void list() {
        final MenuGroup menuGroup1 = new MenuGroup();
        final MenuGroup menuGroup2 = new MenuGroup();
        final List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);

        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        final List<MenuGroup> actual = menuGroupService.list();
        assertThat(actual).containsExactly(menuGroup1, menuGroup2);
    }
}
