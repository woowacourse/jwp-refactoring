package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.Product;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.order.MenuInfo;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.TableGroup;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.repository.TableGroupRepository;
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
        final Product product = Product.ofUnsaved(name, new BigDecimal(price));
        return productRepository.save(product);
    }

    public MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = MenuGroup.ofUnsaved(name);
        return menuGroupRepository.save(menuGroup);
    }

    public Menu saveMenu(final String name,
                         final int price,
                         final Long menuGroupId,
                         final MenuProduct... menuProducts) {
        final Menu menu = Menu.ofUnsaved(name, new BigDecimal(price), menuGroupId);
        final List<MenuProduct> mappedMenuProducts = Arrays.stream(menuProducts)
                .map(menuProduct -> mapMenuToMenuProduct(menu, menuProduct))
                .collect(Collectors.toList());
        menu.changeMenuProducts(mappedMenuProducts);

        return menuRepository.save(menu);
    }

    public MenuInfo saveMenuAndGetInfo(final String name,
                         final int price,
                         final Long menuGroupId,
                         final MenuProduct... menuProducts) {
        final Menu menu = saveMenu(name, price, menuGroupId, menuProducts);
        return new MenuInfo(menu.getId(), menu.getName(), menu.getPrice());
    }

    public OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.ofUnsaved(numberOfGuests, empty);
        return orderTableRepository.save(orderTable);
    }

    public TableGroup saveTableGroup() {
        final TableGroup tableGroup = TableGroup.ofUnsaved();
        return tableGroupRepository.save(tableGroup);
    }

    public List<OrderTable> saveTwoGroupedTables(final TableGroup tableGroup) {
        final OrderTable orderTable1 = saveOrderTable(0, true);
        final OrderTable orderTable2 = saveOrderTable(0, true);
        final OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));
        orderTables.joinGroup(tableGroup.getId());

        return orderTables.getValues();
    }

    public Order saveOrderWithoutItem(final Long orderTableId, final OrderStatus orderStatus) {
        final Order order = new Order(null, orderTableId, orderStatus, LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order saveOrder(final Long orderTableId,
                           final OrderStatus orderStatus,
                           final OrderLineItem... orderLineItems) {
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
        return new OrderLineItem(null, order, orderLineItem.getMenuInfo(), orderLineItem.getQuantity());
    }
}
