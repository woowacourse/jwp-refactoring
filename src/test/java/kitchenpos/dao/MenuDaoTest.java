package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuDaoTest extends DaoTest {

    @Autowired
    private MenuDao menuDao;

    @Test
    void save() throws Exception {
        Menu menu = new Menu("메뉴명", new BigDecimal(100), 1L);
        Menu savedMenu = menuDao.save(menu);
        Menu foundMenu = menuDao
            .findById(savedMenu.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedMenu.getId()).isEqualTo(foundMenu.getId());
        assertThat(savedMenu.getPrice()).isEqualTo(foundMenu.getPrice());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(foundMenu.getMenuGroupId());
    }

    @Test
    void findById() throws Exception {
        Menu menu = new Menu("메뉴명", new BigDecimal(100), 1L);
        Menu savedMenu = menuDao.save(menu);
        Menu foundMenu = menuDao
            .findById(savedMenu.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedMenu.getId()).isEqualTo(foundMenu.getId());
        assertThat(savedMenu.getPrice()).isEqualTo(foundMenu.getPrice());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(foundMenu.getMenuGroupId());
    }

    @Test
    void findAll() {
        menuDao.save(new Menu("메뉴명1", new BigDecimal(100), 1L));
        menuDao.save(new Menu("메뉴명2", new BigDecimal(200), 2L));
        List<Menu> all = menuDao.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void countByIdIn() {
        Menu menu1 = menuDao.save(new Menu("메뉴명1", new BigDecimal(100), 1L));
        Menu menu2 = menuDao.save(new Menu("메뉴명2", new BigDecimal(200), 2L));
        long count = menuDao.countByIdIn(Arrays.asList(
            menu1.getId(),
            menu2.getId()
        ));
        assertThat(count).isEqualTo(2);
    }
}
