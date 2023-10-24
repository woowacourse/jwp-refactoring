package kitchenpos.support;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestSupporter {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    public Product createProduct() {
        final Product product = new Product("name", 10_000);
        return productRepository.save(product);
    }

    public MenuGroup createMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup("name");
        return menuGroupRepository.save(menuGroup);
    }

    public Menu createMenu() {
        final Menu menu = new Menu("name",
                                   Price.from(50_000),
                                   createMenuGroup(),
                                   null);
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(5,
                                                                       menu,
                                                                       createProduct()));
        menu.addMenuProducts(menuProducts);
        return menuRepository.save(menu);
    }

    public OrderTable createOrderTable() {
        final OrderTable orderTable = new OrderTable(null,
                                                     0,
                                                     false);
        return orderTableRepository.save(orderTable);
    }

    public OrderTable createOrderTable(final boolean empty) {
        final OrderTable orderTable = new OrderTable(null,
                                                     0,
                                                     empty);
        return orderTableRepository.save(orderTable);
    }

    public Order createOrder() {
        final OrderTable orderTable = createOrderTable();
        final Menu menu = createMenu();
        final Order order = new Order(orderTable,
                                      OrderStatus.COOKING,
                                      LocalDateTime.now(),
                                      new ArrayList<>());
        final OrderLineItem orderLineItem = new OrderLineItem(order, menu, 1);
        order.addOrderLineItems(List.of(orderLineItem));
        return orderRepository.save(order);
    }

    public Order createOrder(final OrderTable orderTable) {
        final Menu menu = createMenu();
        final Order order = new Order(orderTable,
                                      OrderStatus.COOKING,
                                      LocalDateTime.now(),
                                      new ArrayList<>());
        final OrderLineItem orderLineItem = new OrderLineItem(order, menu, 1);
        order.addOrderLineItems(List.of(orderLineItem));
        return orderRepository.save(order);
    }

    public TableGroup createTableGroup() {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                                                     new ArrayList<>());
        final OrderTable orderTable1 = new OrderTable(null, 0, true);
        final OrderTable orderTable2 = new OrderTable(null, 0, true);
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));
        return tableGroupRepository.save(tableGroup);
    }
}
