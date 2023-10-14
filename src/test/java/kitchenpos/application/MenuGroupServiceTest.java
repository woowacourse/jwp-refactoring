package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class MenuGroupServiceTest extends ServiceTest{

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp(){
        menuGroup = makeMenuGroup();
    }

    @Test
    void 메뉴그룹을_생성한다() {
        Mockito.when(menuGroupDao.save(any(MenuGroup.class)))
                .thenReturn(menuGroup);

        MenuGroup saved = menuGroupService.create(menuGroup);

        assertThat(saved.getId()).isEqualTo(menuGroup.getId());
        assertThat(saved.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void 전체_메뉴그룹을_조회한다() {
        Mockito.when(menuGroupDao.findAll())
                .thenReturn(List.of(new MenuGroup(),new MenuGroup()));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(2);
    }

    private MenuGroup makeMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("한마리통닭");
        return menuGroup;
    }
}
