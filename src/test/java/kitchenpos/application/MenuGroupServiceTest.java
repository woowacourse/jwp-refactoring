package kitchenpos.application;

import static kitchenpos.Fixture.MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    void create() {
        //given
        given(menuGroupDao.save(any(MenuGroup.class)))
                .willReturn(MENU_GROUP);

        //when
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        MenuGroup savedManuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedManuGroup).isEqualTo(MENU_GROUP);
    }

    @Test
    void list() {
        //given
        given(menuGroupDao.findAll())
                .willReturn(List.of(MENU_GROUP));

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).hasSize(1);
    }
}
