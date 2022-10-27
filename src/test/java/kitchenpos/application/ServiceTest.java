package kitchenpos.application;

import static kitchenpos.application.DomainFixture.getProduct;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class ServiceTest {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected OrderTableDao orderTableDao;

    protected Product 상품_등록(Product product) {
        return productService.create(product);
    }

    protected MenuGroup 메뉴_그룹_등록(MenuGroup menuGroup) {
        return menuGroupService.create(menuGroup);
    }

    protected Menu 메뉴_등록(Menu menu) {
        return menuService.create(menu);
    }

    protected OrderTable 테이블_등록(OrderTable table) {
        return tableService.create(table);
    }

    protected TableGroup 단체_지정(TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected Order 주문_등록(Order order) {
        return orderService.create(order);
    }

    protected List<MenuProduct> createMenuProducts() {
        final Product product = 상품_등록(getProduct());
        return List.of(new MenuProduct(null, null, product.getId(), 1));
    }
}
