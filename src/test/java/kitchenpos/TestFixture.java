package kitchenpos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;


@Component
public class TestFixture {

    private ProductService productService;
    private MenuGroupDao menuGroupDao;
    private MenuService menuService;
    private OrderTableDao orderTableDao;

    public TestFixture(ProductService productService, MenuGroupDao menuGroupDao, MenuService menuService,
                       OrderTableDao orderTableDao) {
        this.productService = productService;
        this.menuGroupDao = menuGroupDao;
        this.menuService = menuService;
        this.orderTableDao = orderTableDao;
    }

    public Product 삼겹살() {
        return productService.create(new Product("삼겹살", BigDecimal.valueOf(1000L)));
    }

    public MenuGroup 삼겹살_종류() {
        return menuGroupDao.save(new MenuGroup("삼겹살 종류"));
    }

    public Menu 삼겹살_메뉴() {
        Product product = productService.create(삼겹살());
        MenuGroup menuGroup = menuGroupDao.save(삼겹살_종류());
        List<MenuProduct> menuProducts = new ArrayList<MenuProduct>();
        menuProducts.add(new MenuProduct(product.getId(), 1L));

        return menuService.create(
                new Menu("메뉴", BigDecimal.valueOf(1000L), menuGroup.getId(), menuProducts)
        );
    }
}
