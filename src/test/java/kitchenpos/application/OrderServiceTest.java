package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
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
    private TableDao tableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private OrderCreateRequest order;

    @BeforeEach
    void setupOrder() {
        Table table = new Table(5, false);
        Table table2 = new Table(10, false);

        Table persistTable = tableDao.save(table);
        Table persistTable2 = tableDao.save(table2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(persistTable, persistTable2));
        tableGroupDao.save(tableGroup);

        Product product = new Product("product1", BigDecimal.TEN);
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct(null, persistProduct.getId(), 10);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu("menu1", new Price(BigDecimal.TEN), persistMenuGroup.getId(), Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(persistMenu.getId(), 1L);

        order = new OrderCreateRequest(persistTable.getId(), Arrays.asList(orderLineItem));
    }

    @Test
    void create() {
        OrderResponse persistOrder = orderService.create(order);

        assertAll(
            () -> assertThat(persistOrder.getId()).isNotNull(),
            () -> assertThat(persistOrder.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(persistOrder.getOrderedTime()).isNotNull()
        );
    }

    @Test
    void list() {
        OrderResponse persistOrder = orderService.create(order);

        List<OrderResponse> orders = orderService.list();

        assertAll(
            () -> assertThat(orders).hasSize(1),
            () -> assertThat(orders.get(0).getId()).isNotNull(),
            () -> assertThat(orders.get(0)).isEqualToIgnoringGivenFields(persistOrder, "id", "orderLineItems")
        );
    }

    @Test
    void changeOrderStatus() {
        OrderResponse persistOrder = orderService.create(order);
        OrderStatusChangeRequest request = new OrderStatusChangeRequest("MEAL");

        OrderResponse changedOrder = orderService.changeOrderStatus(persistOrder.getId(), request);

        assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
    }
}
