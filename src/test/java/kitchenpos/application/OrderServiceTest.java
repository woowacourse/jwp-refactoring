package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Order order;

    @BeforeEach
    void setupOrder() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);

        OrderTable orderTable2 = new OrderTable();
        orderTable.setNumberOfGuests(10);

        OrderTable persistOrderTable = orderTableDao.save(orderTable);
        OrderTable persistOrderTable2 = orderTableDao.save(orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(persistOrderTable, persistOrderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());

        tableGroupDao.save(tableGroup);

        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setName("product1");
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(10);
        menuProduct.setProductId(persistProduct.getId());

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();
        menu.setName("menu1");
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuGroupId(persistMenuGroup.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(persistMenu.getId());

        order = new Order();
        order.setOrderTableId(persistOrderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
    }

    @Test
    void create() {
        Order persistOrder = orderService.create(order);

        assertAll(
            () -> assertThat(persistOrder.getId()).isNotNull(),
            () -> assertThat(persistOrder).isEqualToIgnoringGivenFields(order, "id", "orderLineItems")
        );
    }

    @Test
    void list() {
        Order persistOrder = orderService.create(order);

        List<Order> orders = orderService.list();

        assertAll(
            () -> assertThat(orders).hasSize(1),
            () -> assertThat(orders.get(0).getId()).isNotNull(),
            () -> assertThat(orders.get(0)).isEqualToIgnoringGivenFields(persistOrder, "id", "orderLineItems")
        );
    }

    @Test
    void changeOrderStatus() {
        Order persistOrder = orderService.create(order);
        order.setOrderStatus("MEAL");

        Order changedOrder = orderService.changeOrderStatus(persistOrder.getId(), this.order);

        assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
    }
}
