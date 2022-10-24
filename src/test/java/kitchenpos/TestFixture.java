package kitchenpos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;

@Component
@Transactional
public class TestFixture {

    private final ProductService productService;
    private final MenuGroupDao menuGroupDao;
    private final MenuService menuService;
    private final TableGroupService tableGroupService;
    private final OrderLineItemDao orderLineItemDao;

    public TestFixture(ProductService productService, MenuGroupDao menuGroupDao, MenuService menuService,
                       TableGroupService tableGroupService, OrderLineItemDao orderLineItemDao) {
        this.productService = productService;
        this.menuGroupDao = menuGroupDao;
        this.menuService = menuService;
        this.tableGroupService = tableGroupService;
        this.orderLineItemDao = orderLineItemDao;
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
