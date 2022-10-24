package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuDaoTest {

    private MenuDao menuDao;

    @Autowired
    public MenuDaoTest(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Test
    void save() {
        // given
        Menu menu = new Menu(
                "메뉴",
                BigDecimal.valueOf(1000),
                1L,
                List.of(new MenuProduct(1L, 1L, 3), new MenuProduct(1L, 2L, 2)));
        // when
        Menu savedMenu = menuDao.save(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Menu menu = new Menu(
                "메뉴",
                BigDecimal.valueOf(1000),
                1L,
                List.of(new MenuProduct(1L, 1L, 3), new MenuProduct(1L, 2L, 2)));
        Menu savedMenu = menuDao.save(menu);
        // when
        Optional<Menu> foundMenu = menuDao.findById(savedMenu.getId());

        // then
        assertThat(foundMenu).isPresent();
    }

    @Test
    void findAll() {
        // given
        Menu menu = new Menu(
                "메뉴",
                BigDecimal.valueOf(1000),
                1L,
                List.of(new MenuProduct(1L, 1L, 3), new MenuProduct(1L, 2L, 2)));
        menuDao.save(menu);
        // when
        List<Menu> menus = menuDao.findAll();

        // then
        int defaultSize = 6;
        assertThat(menus).hasSize(1 + defaultSize);
    }

    @Test
    void countByIdIn() {
        // given
        Menu menu = new Menu(
                "메뉴",
                BigDecimal.valueOf(1000),
                1L,
                List.of(new MenuProduct(1L, 1L, 3), new MenuProduct(1L, 2L, 2)));
        Menu savedMenu = menuDao.save(menu);

        // when
        long count = menuDao.countByIdIn(List.of(savedMenu.getId()));

        // then
        assertThat(count).isEqualTo(1);
    }
}
