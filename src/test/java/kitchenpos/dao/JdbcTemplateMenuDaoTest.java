package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
class JdbcTemplateMenuDaoTest {
    @Autowired
    private JdbcTemplateMenuDao menuDao;
    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("Menu Group1");

        menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setName("menu");
        menu.setPrice(BigDecimal.valueOf(2000));

        Product product1 = new Product();
        Product product2 = new Product();
        product1.setId(1L);
        product2.setId(2L);
        product1.setPrice(BigDecimal.valueOf(1000));
        product2.setPrice(BigDecimal.valueOf(1000));
        product1.setName("product1");
        product2.setName("product2");

        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct1.setQuantity(1);
        menuProduct2.setQuantity(2);
        menuProduct1.setProductId(1L);
        menuProduct2.setProductId(2L);
        menuProduct1.setMenuId(1L);
        menuProduct2.setMenuId(2L);
    }

    @Test
    void save() {
        Menu savedMenu = menuDao.save(menu);

        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getPrice().toBigInteger()).isEqualTo(menu.getPrice().toBigInteger());
    }

    @Test
    void findById() {
        Menu menu = menuDao.findById(1L).get();

        assertThat(menu.getId()).isEqualTo(1L);
        assertThat(menu.getPrice().toBigInteger()).isEqualTo(16000);
        assertThat(menu.getName()).isEqualTo("후라이드치킨");
        assertThat(menu.getMenuGroupId()).isEqualTo(2L);

    }

    @Test
    void findAll() {
        List<Menu> menus = menuDao.findAll();
        menuDao.save(menu);
        List<Menu> savedMenus = menuDao.findAll();

        assertThat(savedMenus.size()).isEqualTo(menus.size() + 1);
    }

    @Test
    void countByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Long count = menuDao.countByIdIn(ids);

        assertThat(count).isEqualTo(3);
    }
}
