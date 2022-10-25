package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    protected ProductDao productDao;

    @Mock
    protected MenuDao menuDao;

    @Mock
    protected MenuGroupDao menuGroupDao;

    @Mock
    protected MenuProductDao menuProductDao;

    @Mock
    protected OrderDao orderDao;

    @Mock
    protected OrderTableDao orderTableDao;

    @Mock
    protected OrderLineItemDao orderLineItemDao;

    @Mock
    protected TableGroupDao tableGroupDao;

    @InjectMocks
    protected ProductService productService;

    @InjectMocks
    protected MenuService menuService;

    @InjectMocks
    protected MenuGroupService menuGroupService;

    @InjectMocks
    protected OrderService orderService;

    @InjectMocks
    protected TableService tableService;

    @InjectMocks
    protected TableGroupService tableGroupService;

    protected Menu getMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    protected MenuGroup getMenuGroup(Long id, String menuName) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(menuName);
        return menuGroup;
    }

    protected Product getProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    protected MenuProduct getMenuProduct(Long id, Long menuId, Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(id);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected OrderTable getOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }

    protected TableGroup getTableGroup(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }

    protected Order getOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                             List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        orderLineItems
                .forEach(it -> it.setOrderId(id));
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    protected OrderLineItem getOrderLineItem(Long seq, Long orderId, Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }
}
