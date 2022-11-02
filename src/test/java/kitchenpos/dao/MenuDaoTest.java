package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("MenuDao 는 ")
@SpringTestWithData
class MenuDaoTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴를 저장한다.")
    @Test
    void save() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroupName"));

        final Menu menu = menuDao.save(
                new Menu("menuName", BigDecimal.valueOf(0L), menuGroup.getId(), Collections.emptyList()));

        assertThat(menu.getId()).isGreaterThanOrEqualTo(1L);
    }

    @DisplayName("특정 메뉴를 조회한다.")
    @Test
    void findById() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroupName"));
        final Menu menu = menuDao.save(
                new Menu("menuName", BigDecimal.valueOf(0L), menuGroup.getId(), Collections.emptyList()));

        final Menu actual = menuDao.findById(menu.getId())
                .get();

        assertThat(actual.getId()).isEqualTo(actual.getId());
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findAll() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroupName"));
        final Menu menu = menuDao.save(
                new Menu("menuName", BigDecimal.valueOf(0L), menuGroup.getId(), Collections.emptyList()));

        final List<Menu> actual = menuDao.findAll();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(menu.getId())
        );
    }

    @DisplayName("특정 메뉴들의 총 갯수를 조회한다.")
    @Test
    void countByIdIn() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroupName"));
        final Menu menu = menuDao.save(
                new Menu("menuName", BigDecimal.valueOf(0L), menuGroup.getId(), Collections.emptyList()));

        final long actual = menuDao.countByIdIn(List.of(menu.getId()));

        assertThat(actual).isEqualTo(1);
    }
}
