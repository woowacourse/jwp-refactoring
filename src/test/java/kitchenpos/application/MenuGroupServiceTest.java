package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("MenuGroup을 생성한다.")
    @Test
    void create() {
        MenuGroup newMenuGroup = new MenuGroup();
        newMenuGroup.setId(1L);
        newMenuGroup.setName("new menu group");

        given(menuGroupDao.save(any()))
                .willReturn(newMenuGroup);

        MenuGroup actual = menuGroupService.create(newMenuGroup);
        assertThat(actual).isEqualTo(newMenuGroup);
    }

    @DisplayName("저장된 MenuGroup 목록을 가져온다.")
    @Test
    void list() {
        // given
        MenuGroup menu1 = new MenuGroup();
        menu1.setId(1L);
        menu1.setName("first menu group");

        MenuGroup menu2 = new MenuGroup();
        menu2.setId(2L);
        menu2.setName("second menu group");

        MenuGroup menu3 = new MenuGroup();
        menu3.setId(3L);
        menu3.setName("third menu group");

        List<MenuGroup> menuGroups = List.of(menu1, menu2, menu3);
        given(menuGroupDao.findAll())
                .willReturn(menuGroups);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).isEqualTo(menuGroups);
    }
}
