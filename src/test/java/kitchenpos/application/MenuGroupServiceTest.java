package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @MockBean
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴그룹을_생성한다() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("한마리통닭");

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
}
