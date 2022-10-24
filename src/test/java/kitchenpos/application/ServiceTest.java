package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
public class ServiceTest {

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

    protected Product 제품을_저장한다(final Product product) {
        return productService.create(product);
    }

    protected MenuGroup 메뉴그룹을_저장한다(final MenuGroup menuGroup) {
        return menuGroupService.create(menuGroup);
    }

    protected Menu 메뉴를_저장한다(final Menu menu) {
        return menuService.create(menu);
    }

    protected OrderTable 주문테이블을_저장한다(final OrderTable orderTable) {
        return tableService.create(orderTable);
    }

    protected TableGroup 테이블_그룹을_저장한다(final TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected Order 주문을_저장한다(final Order order) {
        return orderService.create(order);
    }

    protected Order 주문_상태를_변경한다(final long orderId, final OrderStatus orderStatus) {
        return orderService.changeOrderStatus(orderId, new Order(null, orderStatus.name(), null));
    }
}
