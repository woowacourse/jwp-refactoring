package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.Fixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        given(menuGroupDao.save(any(MenuGroup.class)))
                .willReturn(Fixtures.menuGroup(1, "메뉴그룹1"));

        // when
        MenuGroup createdMenuGroup = menuGroupService.create(new MenuGroup());

        // then
        assertThat(createdMenuGroup.getId()).isEqualTo(1);
        assertThat(createdMenuGroup.getName()).isEqualTo("메뉴그룹1");
    }

    @Test
    void list() {
        // given
        given(menuGroupDao.findAll())
                .willReturn(Collections.singletonList(
                        Fixtures.menuGroup(1, "메뉴그룹1")));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(1);
        MenuGroup menuGroup = menuGroups.get(0);
        assertThat(menuGroup.getId()).isEqualTo(1);
        assertThat(menuGroup.getName()).isEqualTo("메뉴그룹1");
    }
}