package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataSupport {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuProductDao menuProductDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;

    public Product saveProduct(final String name, final int price) {
        final Product product = Product.ofNew(name, new BigDecimal(price));
        return productDao.save(product);
    }

    public MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = MenuGroup.ofNew(name);
        return menuGroupDao.save(menuGroup);
    }

    public Menu saveMenu(final String name,
                         final int price,
                         final Long menuGroupId,
                         final MenuProduct... menuProducts) {
        final Menu menu = Menu.ofNew(name, new BigDecimal(price), menuGroupId);
        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> savedMenuProducts = Arrays.stream(menuProducts)
                .map(menuProduct -> {
                    menuProduct.setMenuId(savedMenu.getId());
                    return menuProductDao.save(menuProduct);
                })
                .collect(Collectors.toList());
        savedMenu.addMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public TableGroup saveTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroupDao.save(tableGroup);
    }

    public OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.ofNew(numberOfGuests, empty);
        return orderTableDao.save(orderTable);
    }

    public OrderTable saveOrderTableWithGroup(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.ofNew(numberOfGuests, empty);
        orderTable.joinGroup(tableGroupId);

        return orderTableDao.save(orderTable);
    }

    public Order saveOrder(final Long orderTableId, final String orderStatus, final OrderLineItem... orderLineItems) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = Arrays.stream(orderLineItems)
                .map(orderLineItem -> {
                    orderLineItem.setOrderId(orderId);
                    return orderLineItemDao.save(orderLineItem);
                })
                .collect(Collectors.toList());
        savedOrder.setOrderLineItems(savedOrderLineItems);
        return savedOrder;
    }

    public OrderTable findOrderTable(final Long id) {
        return orderTableDao.findById(id)
                .get();
    }

    public Order findOrder(final Long id) {
        return orderDao.findById(id)
                .get();
    }

    public List<OrderTable> findTableByTableGroupId(final Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId);
    }
}
