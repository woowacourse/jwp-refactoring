package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroup savedMenuGroup = new MenuGroup();
        savedMenuGroup.setId(1L);
        savedMenuGroup.setName("menu-group");
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(savedMenuGroup);

        // when
        MenuGroup actual = menuGroupService.create(new MenuGroup());

        // then
        Assertions.assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("menu-group")
        );
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menu-group");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual)
                .hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        Arrays.asList(
                                new MenuGroup(null, "menu-group")
                        )
                );
    }

}
