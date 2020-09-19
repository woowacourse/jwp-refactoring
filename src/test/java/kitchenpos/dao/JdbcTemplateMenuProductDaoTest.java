package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.MenuProduct;

@SpringBootTest
class JdbcTemplateMenuProductDaoTest {
    @Autowired
    private JdbcTemplateMenuProductDao menuProductDao;

    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        menuProduct1 = new MenuProduct();
        menuProduct2 = new MenuProduct();
        menuProduct1.setQuantity(1);
        menuProduct2.setQuantity(2);
        menuProduct1.setProductId(1L);
        menuProduct2.setProductId(2L);
        menuProduct1.setMenuId(1L);
        menuProduct2.setMenuId(3L);
    }

    @Test
    void save() {
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct1);
        assertThat(savedMenuProduct.getMenuId()).isEqualTo(menuProduct1.getMenuId());
        assertThat(savedMenuProduct.getProductId()).isEqualTo(menuProduct1.getProductId());
        assertThat(savedMenuProduct.getQuantity()).isEqualTo(menuProduct1.getQuantity());
    }

    @Test
    void findById() {
        MenuProduct menuProduct = menuProductDao.findById(1L).get();

        assertThat(menuProduct.getSeq()).isEqualTo(1L);
        assertThat(menuProduct.getMenuId()).isEqualTo(1L);
        assertThat(menuProduct.getProductId()).isEqualTo(1L);
        assertThat(menuProduct.getQuantity()).isEqualTo(1L);
    }

    @Test
    void findAll() {
        List<MenuProduct> menuProducts = menuProductDao.findAll();
        menuProductDao.save(menuProduct1);
        List<MenuProduct> savedMenuProducts = menuProductDao.findAll();

        assertThat(savedMenuProducts.size()).isEqualTo(menuProducts.size() + 1);

    }

    @Test
    void findAllByMenuId() {
        menuProductDao.save(menuProduct2);
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(3L);

        assertThat(menuProducts.size()).isEqualTo(2);
    }
}
