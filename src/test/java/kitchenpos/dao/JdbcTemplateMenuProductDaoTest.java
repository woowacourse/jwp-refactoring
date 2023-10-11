package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateMenuProductDaoTest extends JdbcTemplateTest {

    private MenuProductDao menuProductDao;

    @BeforeEach
    void setUp() {
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @Test
    void 메뉴_상품_관계를_저장한다() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1L);
        MenuProduct saved = menuProductDao.save(menuProduct);

        assertThat(saved.getMenuId()).isEqualTo(1L);
        assertThat(saved.getProductId()).isEqualTo(1L);
        assertThat(saved.getQuantity()).isEqualTo(1L);
    }

    @Test
    void 식별자로_메뉴_상품_관계를_조회한다() {
        MenuProduct menuProduct = menuProductDao.findById(1L).get();

        assertThat(menuProduct.getSeq()).isEqualTo(1L);
    }

    @Test
    void 모든_메뉴_상품_관계를_조회한다() {
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        assertThat(menuProducts.size()).isEqualTo(6);
    }

    @Test
    void 메뉴_식별자를_기반으로_모든_메뉴_상품_관계를_찾는다() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1L);
        menuProductDao.save(menuProduct);

        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(1L);

        assertThat(menuProducts.size()).isEqualTo(2);
    }
}
