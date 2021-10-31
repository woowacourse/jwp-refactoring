package kitchenpos.integration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProductDao;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.ordertable.domain.OrderTableDao;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.tablegroup.domain.TableGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixtureMaker {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuProductDao menuProductDao;

    public List<MenuProduct> createMenuProducts(Long menuGroupId) {
        Product product1 = productDao.save(new Product("상품1", new BigDecimal(16_000)));
        Product product2 = productDao.save(new Product("상품2", new BigDecimal(17_000)));
        Product product3 = productDao.save(new Product("상품3", new BigDecimal(18_000)));

        Menu menu = menuDao.save(new Menu("메뉴1", new BigDecimal(16_000), menuGroupId));

        MenuProduct menuProduct1 = new MenuProduct(product1.getId(), 1, menu);
        MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 1, menu);
        MenuProduct menuProduct3 = new MenuProduct(product3.getId(), 1, menu);

        return Arrays.asList(
            menuProduct1, menuProduct2, menuProduct3
        );
    }

    public MenuGroup createMenuGroup() {
        return menuGroupDao.save(new MenuGroup("메뉴 그룹"));
    }

    public OrderTable createOrderTableForNotEmpty() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        return orderTableDao.save(new OrderTable(tableGroup, 10, false));
    }

    public OrderTable createOrderTableForEmpty() {
        return orderTableDao.save(new OrderTable(10, true));
    }

    public List<OrderLineItem> createOrderLineItems() {
        Order order = createOrder();
        Menu menu = createMenu();
        OrderLineItem orderLineItem = orderLineItemDao.save(new OrderLineItem(order, menu.getId(), 10));
        return Arrays.asList(orderLineItem);
    }

    public Menu createMenu() {
        MenuGroup menuGroup = createMenuGroup();
        List<MenuProduct> menuProducts = createMenuProducts(menuGroup.getId());
        return menuDao.save(new Menu("메뉴", new BigDecimal(1000), menuGroup.getId(), new MenuProducts(menuProducts)));
    }

    public Order createOrder() {
        OrderTable orderTable = createOrderTableForNotEmpty();
        return orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
    }

    public TableGroup createTableGroup() {
        return tableGroupDao.save(new TableGroup(LocalDateTime.now()));
    }
}
