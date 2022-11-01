package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.ordertable.TableGroup;
import kitchenpos.domain.ordertable.TableGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataSupport {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Product saveProduct(final String name, final int price) {
        final Product product = Product.ofNew(name, new BigDecimal(price));
        return productRepository.save(product);
    }

    public MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = MenuGroup.ofNew(name);
        return menuGroupRepository.save(menuGroup);
    }

    public Menu saveMenu(final String name,
                         final int price,
                         final Long menuGroupId,
                         final MenuProduct... menuProducts) {
        final Menu menu = Menu.ofNew(name, new BigDecimal(price), menuGroupId);
        final List<MenuProduct> mappedMenuProducts = Arrays.stream(menuProducts)
                .map(menuProduct -> mapMenuToMenuProduct(menu, menuProduct))
                .collect(Collectors.toList());
        menu.changeMenuProducts(mappedMenuProducts);

        return menuRepository.save(menu);
    }

    public TableGroup saveTableGroup() {
        final OrderTable orderTable1 = saveOrderTable(0, true);
        final OrderTable orderTable2 = saveOrderTable(0, true);
        final TableGroup tableGroup = TableGroup.ofNew(Arrays.asList(orderTable1, orderTable2));

        return tableGroupRepository.save(tableGroup);
    }

    public OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.ofNew(numberOfGuests, empty);
        return orderTableRepository.save(orderTable);
    }

    public Order saveOrderWithoutItem(final Long orderTableId, final OrderStatus orderStatus) {
        final Order order = new Order(null, orderTableId, orderStatus, LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order saveOrder(final Long orderTableId, final OrderStatus orderStatus, final OrderLineItem... orderLineItems) {
        final Order order = new Order(null, orderTableId, orderStatus, LocalDateTime.now());
        final List<OrderLineItem> mappedOrderLineItems = Arrays.stream(orderLineItems)
                .map(orderLineItem -> mapOrderToOrderLineItem(order, orderLineItem))
                .collect(Collectors.toList());
        order.changeOrderLineItems(mappedOrderLineItems);

        orderRepository.save(order);
        return order;
    }

    public OrderTable findOrderTable(final Long id) {
        return orderTableRepository.findById(id)
                .get();
    }

    public Order findOrder(final Long id) {
        return orderRepository.findById(id)
                .get();
    }

    private MenuProduct mapMenuToMenuProduct(final Menu menu, final MenuProduct menuProduct) {
        return new MenuProduct(
                null, menu, menuProduct.getProductId(), menuProduct.getQuantity(), menuProduct.getPrice());
    }

    private OrderLineItem mapOrderToOrderLineItem(final Order order, final OrderLineItem orderLineItem) {
        return new OrderLineItem(null, order, orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }
}
