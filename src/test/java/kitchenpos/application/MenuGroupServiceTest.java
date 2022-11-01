package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fakedao.MenuGroupFakeDao;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;


    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // when
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertThat(menuGroupDao.findById(actual.getId())).isPresent();
    }

    @DisplayName("모든 메뉴 그룹을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup1 = new MenuGroup("menuGroup1");
        MenuGroup menuGroup2 = new MenuGroup("menuGroup2");
        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.stream()
                        .map(MenuGroup::getName))
                        .containsExactly("menuGroup1", "menuGroup2")
        );
    }
}
