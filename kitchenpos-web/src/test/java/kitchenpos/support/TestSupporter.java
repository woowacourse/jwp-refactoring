package kitchenpos.support;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.vo.MenuInfo;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
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
                                   createMenuGroup().getId(),
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

    public OrderTable createOrderTable(final Long tableGroupId) {
        final OrderTable orderTable = new OrderTable(tableGroupId,
                                                     0,
                                                     false);
        return orderTableRepository.save(orderTable);
    }

    public Order createOrder() {
        final OrderTable orderTable = createOrderTable();
        final Menu menu = createMenu();
        final Order order = new Order(orderTable,
                                      OrderStatus.COOKING,
                                      LocalDateTime.now(),
                                      new ArrayList<>());
        final OrderLineItem orderLineItem = new OrderLineItem(order, MenuInfo.from(menu), 1);
        order.addOrderLineItems(List.of(orderLineItem));
        return orderRepository.save(order);
    }

    public Order createOrder(final OrderTable orderTable) {
        final Menu menu = createMenu();
        final Order order = new Order(orderTable,
                                      OrderStatus.COOKING,
                                      LocalDateTime.now(),
                                      new ArrayList<>());
        final OrderLineItem orderLineItem = new OrderLineItem(order, MenuInfo.from(menu), 1);
        order.addOrderLineItems(List.of(orderLineItem));
        return orderRepository.save(order);
    }

    public TableGroup createTableGroup() {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        return tableGroupRepository.save(tableGroup);
    }
}
