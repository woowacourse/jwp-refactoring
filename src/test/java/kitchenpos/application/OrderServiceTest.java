package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;

@SpringBootTest
@Sql(value = "/truncate.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("주문 생성 시 주문항목이 없을 시 예외가 발생한다.")
    @Test
    void createWithNoOrderItem() {
        Order order = createOrder(1L, LocalDateTime.now(), null, OrderStatus.COOKING, 1L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 주문 항목이 중복되거나 없는 주문 항목일 시 예외가 발생한다.")
    @Test
    void createWithInvalidOrderItem() {
        MenuGroup savedMenuGroup = getMenuGroup();
        Product savedProduct = saveProduct();
        Menu savedMenu = saveMenu(savedMenuGroup, savedProduct);

        OrderLineItem 주문된_치킨세트 = createOrderLineItem(null, savedMenu.getId(), 3);

        Order order = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트, 주문된_치킨세트), OrderStatus.COOKING, 1L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 주문 테이블이 없는 경우 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        MenuGroup savedMenuGroup = getMenuGroup();
        Product savedProduct = saveProduct();
        Menu savedMenu = saveMenu(savedMenuGroup, savedProduct);

        OrderLineItem 주문된_치킨세트 = createOrderLineItem(null, savedMenu.getId(), 3);

        Table table = createTable(null, false, null, 5);
        orderTableDao.save(table);

        Order order = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트), OrderStatus.COOKING, -1L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시 주문테이블의 손님이 없을 경우 예외가 발생한다.")
    @Test
    void createWithEmptyTable() {
        MenuGroup savedMenuGroup = getMenuGroup();
        Product savedProduct = saveProduct();
        Menu savedMenu = saveMenu(savedMenuGroup, savedProduct);

        OrderLineItem 주문된_치킨세트 = createOrderLineItem(null, savedMenu.getId(), 3);

        Table table = createTable(null, true, null, 5);
        Table savedTable = orderTableDao.save(table);

        Order order = createOrder(1L, LocalDateTime.now(), Arrays.asList(주문된_치킨세트), OrderStatus.COOKING,
            savedTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        MenuGroup savedMenuGroup = getMenuGroup();
        Product savedProduct = saveProduct();
        Menu savedMenu = saveMenu(savedMenuGroup, savedProduct);
        Table savedTable = saveOrderTable();

        OrderLineItem 주문된_치킨세트 = createOrderLineItem(null, savedMenu.getId(), 3);

        Order order = createOrder(null, LocalDateTime.now(), Arrays.asList(주문된_치킨세트), OrderStatus.COOKING,
            savedTable.getId());

        Order savedOrder = orderService.create(order);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(savedTable.getId())
        );
    }

    @DisplayName("주문 리스트를 조회한다")
    @Test
    void list() {
        MenuGroup savedMenuGroup = getMenuGroup();
        Product savedProduct = saveProduct();
        Menu savedMenu = saveMenu(savedMenuGroup, savedProduct);

        OrderLineItem 주문된_치킨세트 = createOrderLineItem(null, savedMenu.getId(), 3);

        Table savedTable = saveOrderTable();

        Order order = createOrder(null, LocalDateTime.now(), Arrays.asList(주문된_치킨세트), OrderStatus.COOKING,
            savedTable.getId());

        Order savedOrder = orderService.create(order);

        List<Order> actual = orderService.list();

        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual.get(0).getOrderStatus()).isEqualTo(savedOrder.getOrderStatus()),
            () -> assertThat(actual.get(0).getOrderTableId()).isEqualTo(savedOrder.getOrderTableId())
        );
    }

    private Table saveOrderTable() {
        Table table = createTable(null, false, null, 5);
        return orderTableDao.save(table);
    }

    @DisplayName("주문 상태를 변경할 때 주문이 없을 시 예외가 발생한다.")
    @Test
    void changeOrderStatusWithInvalidOrder() {
        Order order = createOrder(1L, LocalDateTime.now(), null, OrderStatus.COOKING, 2L);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태를 변경할 때 주문상태가 'COMPLETION' 인 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWhenCompletion() {
        Order order = createOrder(1L, LocalDateTime.now(), null, OrderStatus.COMPLETION, 2L);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        MenuGroup savedMenuGroup = getMenuGroup();
        Product savedProduct = saveProduct();
        Menu savedMenu = saveMenu(savedMenuGroup, savedProduct);
        Table savedTable = saveOrderTable();

        OrderLineItem 주문된_치킨세트 = createOrderLineItem(null, savedMenu.getId(), 3);

        Order order = createOrder(null, LocalDateTime.now(), Arrays.asList(주문된_치킨세트), OrderStatus.COOKING,
            savedTable.getId());

        Order savedOrder = orderService.create(order);

        Order actual = orderService.changeOrderStatus(savedOrder.getId(),
            createOrder(null, LocalDateTime.now(), null, OrderStatus.MEAL, 2L));

        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
    }

    private Menu saveMenu(MenuGroup savedMenuGroup, Product savedProduct) {
        MenuProduct menuProduct = createMenuProduct(null, savedProduct.getId(), 1L);
        Menu 치킨세트 = createMenu(null, savedMenuGroup.getId(), Arrays.asList(menuProduct), "둘둘치킨",
            BigDecimal.valueOf(1900L));

        return menuDao.save(치킨세트);
    }

    private Product saveProduct() {
        Product product = createProduct(null, "양념치킨", BigDecimal.valueOf(2000L));
        return productDao.save(product);
    }

    private MenuGroup getMenuGroup() {
        MenuGroup menuGroup = createMenuGroup(null, "치킨류");
        return menuGroupDao.save(menuGroup);
    }
}