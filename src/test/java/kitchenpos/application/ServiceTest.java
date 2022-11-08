package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenuGroup;
import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static kitchenpos.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.fixture.OrderFixture.createOrderRequest;
import static kitchenpos.fixture.OrderFixture.updatedOrderStatusRequest;
import static kitchenpos.fixture.ProductFixture.createProductRequest;
import static kitchenpos.fixture.TableFixture.createOrderTableRequest;
import static kitchenpos.fixture.TableFixture.createTableGroupRequest;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.table.domain.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
abstract class ServiceTest {

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected Validator validator;

    protected MenuGroup saveMenuGroup(String name) {
        return menuGroupService.create(createMenuGroup(name));
    }

    protected Product saveProduct(String name, BigDecimal price) {
        return productService.create(createProductRequest(name, price));
    }

    protected Menu saveMenu(String menuName, MenuGroup menuGroup, Product product) {
        MenuProduct menuProduct = createMenuProduct(product.getId(), 1, product.getPrice());
        MenuRequest request = createMenuRequest(menuName,
                product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())), menuGroup.getId(),
                menuProduct);
        return menuService.create(request);
    }

    protected OrderTable saveOrderTable(int numberOfGuests, boolean empty) {
        return tableService.create(createOrderTableRequest(numberOfGuests, empty));
    }

    protected TableGroup saveTableGroup(OrderTable... orderTables) {
        return tableGroupService.create(createTableGroupRequest(orderTables));
    }

    protected Order saveOrder(OrderTable orderTable, Menu... menus) {
        return orderService.create(createOrderRequest(orderTable, menus));
    }

    protected Order updateOrder(Order order, String status) {
        return orderService.changeOrderStatus(order.getId(), updatedOrderStatusRequest(status));
    }
}
