package kitchenpos.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataSupport {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuRepository menuRepository;
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
        return productRepository.save(product);
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
        final List<MenuProduct> mappedMenuProducts = Arrays.stream(menuProducts)
                .map(menuProduct -> mapManuProductWithMenu(menu, menuProduct))
                .collect(Collectors.toList());
        menu.changeMenuProducts(mappedMenuProducts);

        return menuRepository.save(menu);
    }

    public TableGroup saveTableGroup() {
        final TableGroup tableGroup = TableGroup.ofNew();
        return tableGroupDao.save(tableGroup);
    }

    public OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.ofNew(numberOfGuests, empty);
        return orderTableDao.save(orderTable);
    }

    public OrderTable saveOrderTableWithGroup(final Long tableGroupId, final int numberOfGuests) {
        final OrderTable orderTable = OrderTable.ofNew(numberOfGuests, true);
        orderTable.joinGroup(tableGroupId);

        return orderTableDao.save(orderTable);
    }

    public Order saveOrder(final Long orderTableId, final String orderStatus, final OrderLineItem... orderLineItems) {
        final Order order =
                new Order(null, orderTableId, orderStatus, LocalDateTime.now(), Arrays.asList(orderLineItems));
        orderDao.save(order);

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.order(order);
            orderLineItemDao.save(orderLineItem);
        }
        return order;
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

    private MenuProduct mapManuProductWithMenu(final Menu menu, final MenuProduct menuProduct) {
        return new MenuProduct(null, menu, menuProduct.getProductId(), menuProduct.getQuantity(),
                menuProduct.getPrice());
    }
}
