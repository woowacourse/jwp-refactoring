package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateMenuDaoTest extends JdbcTemplateTest{

    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @Test
    void 메뉴를_저장한다() {
        Menu menu = new Menu();
        menu.setName("name");
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(1L);
        Menu newMenu = menuDao.save(menu);

        assertThat(menu.getName()).isEqualTo(newMenu.getName());
        assertThat(menu.getPrice().longValue()).isEqualTo(newMenu.getPrice().longValue());
        assertThat(menu.getMenuGroupId()).isEqualTo(newMenu.getMenuGroupId());
    }

    @Test
    void 식별자로_메뉴를_찾는다() {
        Menu menu = menuDao.findById(1L).get();

        assertThat(menu.getId()).isEqualTo(1L);

    }

    @Test
    void findAll() {
        List<Menu> menus = menuDao.findAll();

        assertThat(menus.size()).isEqualTo(6);
    }

    @Test
    void countByIdIn() {
        long ids = menuDao.countByIdIn(List.of(1L, 2L, 3L));
        assertThat(ids).isEqualTo(3L);
    }
}
