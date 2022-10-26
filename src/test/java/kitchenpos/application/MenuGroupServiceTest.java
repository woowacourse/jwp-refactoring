package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹1");

        MenuGroup actual = menuGroupService.create(menuGroup);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo("메뉴그룹1");
        });
    }

    @Test
    void 전체_메뉴_그룹을_조회할_수_있다() {
        MenuGroup menuGroup1 = new MenuGroup("메뉴그룹1");
        MenuGroup menuGroup2 = new MenuGroup("메뉴그룹2");

        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(2);
    }
}
