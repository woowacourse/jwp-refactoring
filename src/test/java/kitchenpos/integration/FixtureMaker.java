package kitchenpos.integration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
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
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
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
        // 상품 생성
        Product product1 = productDao.save(new Product("상품1", new BigDecimal(16_000)));
        Product product2 = productDao.save(new Product("상품2", new BigDecimal(17_000)));
        Product product3 = productDao.save(new Product("상품3", new BigDecimal(18_000)));

        // 메뉴 생성
        Menu menu = menuDao.save(new Menu("메뉴1", new BigDecimal(16_000), menuGroupId));

        // 메뉴를 구성하는 상품 생성
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
        return orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));
    }

    public OrderTable createOrderTableForEmpty() {
        return orderTableDao.save(new OrderTable(10, true));
    }

    public List<OrderLineItem> createOrderLineItems() {
        Order order = createOrder();
        Menu menu = createMenu();
        OrderLineItem orderLineItem = orderLineItemDao.save(new OrderLineItem(order.getId(), menu.getId(), 10));
        return Arrays.asList(orderLineItem);
    }

    public Menu createMenu() {
        MenuGroup menuGroup = createMenuGroup();
        return menuDao.save(new Menu("메뉴", new BigDecimal(1000), menuGroup.getId()));
    }

    public Order createOrder() {
        OrderTable orderTable = createOrderTableForNotEmpty();
        return orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
    }

    public TableGroup createTableGroup() {
        return tableGroupDao.save(new TableGroup(LocalDateTime.now()));
    }
}
