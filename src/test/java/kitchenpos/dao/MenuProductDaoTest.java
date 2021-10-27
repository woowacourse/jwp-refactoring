package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuProductDaoTest extends DaoTest {
    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void save() throws Exception {
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(100), 1L));
        Product product = productDao.save(new Product("상품명", new BigDecimal(100)));
        MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), menu));
        MenuProduct foundMenuProduct = menuProductDao
            .findById(savedMenuProduct.getSeq())
            .orElseThrow(() -> new Exception());
        assertThat(savedMenuProduct.getSeq()).isEqualTo(foundMenuProduct.getSeq());
        assertThat(savedMenuProduct.getProductId()).isEqualTo(foundMenuProduct.getProductId());
        assertThat(savedMenuProduct.getMenuId()).isEqualTo(foundMenuProduct.getMenuId());
        assertThat(savedMenuProduct.getQuantity()).isEqualTo(foundMenuProduct.getQuantity());
    }

    @Test
    void findById() throws Exception {
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(100), 1L));
        Product product = productDao.save(new Product("상품명", new BigDecimal(100)));
        MenuProduct savedMenuProduct = menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), menu));
        MenuProduct foundMenuProduct = menuProductDao
            .findById(savedMenuProduct.getSeq())
            .orElseThrow(() -> new Exception());
        assertThat(savedMenuProduct.getSeq()).isEqualTo(foundMenuProduct.getSeq());
        assertThat(savedMenuProduct.getProductId()).isEqualTo(foundMenuProduct.getProductId());
        assertThat(savedMenuProduct.getMenuId()).isEqualTo(foundMenuProduct.getMenuId());
        assertThat(savedMenuProduct.getQuantity()).isEqualTo(foundMenuProduct.getQuantity());
    }

    @Test
    void findAll() {
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(100), 1L));
        Product product = productDao.save(new Product("상품명", new BigDecimal(100)));
        menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), menu));
        menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), menu));
        List<MenuProduct> menuProducts = menuProductDao.findAll();
        assertThat(menuProducts).hasSize(2);
    }

    @Test
    void findAllByMenuId() {
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(100), 1L));
        Product product = productDao.save(new Product("상품명", new BigDecimal(100)));
        menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), menu));
        menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), menu));
        menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), menu));
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
        assertThat(menuProducts).hasSize(3);
    }
}
