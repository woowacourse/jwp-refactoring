package kitchenpos.integration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
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
    private JdbcTemplateMenuDao menuDao;

    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @Autowired
    private JdbcTemplateProductDao productDao;

    @Autowired
    private JdbcTemplateTableGroupDao tableGroupDao;

    @Autowired
    private JdbcTemplateOrderDao orderDao;

    @Autowired
    private JdbcTemplateOrderTableDao orderTableDao;

    @Autowired
    private JdbcTemplateOrderLineItemDao orderLineItemDao;

    public List<MenuProduct> createMenuProducts() {
        // 상품 생성
        Product product1 = productDao.save(new Product("상품1", new BigDecimal(16_000)));
        Product product2 = productDao.save(new Product("상품2", new BigDecimal(17_000)));
        Product product3 = productDao.save(new Product("상품3", new BigDecimal(18_000)));

        // 메뉴 그룹 생성
        MenuGroup menuGroup = createMenuGroup();

        // 메뉴 생성
        Menu menu1 = menuDao.save(new Menu("메뉴1", new BigDecimal(16_000), menuGroup.getId()));
        Menu menu2 = menuDao.save(new Menu("메뉴2", new BigDecimal(17_000), menuGroup.getId()));
        Menu menu3 = menuDao.save(new Menu("메뉴3", new BigDecimal(18_000), menuGroup.getId()));

        // 메뉴를 구성하는 상품 생성
        MenuProduct menuProduct1 = new MenuProduct(menu1.getId(), product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(menu2.getId(), product2.getId(), 2);
        MenuProduct menuProduct3 = new MenuProduct(menu3.getId(), product3.getId(), 3);

        return Arrays.asList(
            menuProduct1, menuProduct2, menuProduct3
        );
    }

    public MenuGroup createMenuGroup() {
        return menuGroupDao.save(new MenuGroup("메뉴 그룹"));
    }

    public OrderTable createOrderTable() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        return orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));
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
        OrderTable orderTable = createOrderTable();
        return orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
    }
}
