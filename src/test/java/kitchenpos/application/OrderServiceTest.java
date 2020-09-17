package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    private Order order;

    @Autowired
    private MenuGroupDao menuGroupDao;
    private MenuGroup menuGroup;

    @Autowired
    private ProductDao productDao;
    private Product product;

    private MenuProduct menuProduct;

    @Autowired
    private MenuService menuService;
    private Menu menu;

    @Autowired
    private OrderTableDao tableDao;
    private OrderTable table;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.builder()
            .name("강정메뉴")
            .build();
        menuGroup = menuGroupDao.save(menuGroup);

        product = Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .build();
        product = productDao.save(product);

        menuProduct = MenuProduct.builder()
            .productId(product.getId())
            .quantity(1)
            .build();

        menu = Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .menuGroupId(menuGroup.getId())
            .menuProducts(Arrays.asList(menuProduct))
            .build();
        menu = menuService.create(menu);

        table = OrderTable.builder()
            .empty(false)
            .build();
        table = tableDao.save(table);

        OrderLineItem orderLineItem = OrderLineItem.builder()
            .menuId(menu.getId())
            .quantity(2)
            .build();

        order = Order.builder()
            .orderTableId(table.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderLineItems(Arrays.asList(orderLineItem))
            .orderedTime(LocalDateTime.now())
            .build();
    }

    @DisplayName("주문 추가")
    @Test
    void create() {
        Order create = orderService.create(order);

        assertAll(
            () -> assertThat(create.getId()).isNotNull(),
            () -> assertThat(create.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(create.getOrderLineItems().get(0).getSeq()).isNotNull()
        );
    }

    @DisplayName("[예외] 주문 항목이 없는 주문 추가")
    @Test
    void create_Fail_With_NoOrderLineItem() {
        Order order = Order.builder()
            .orderTableId(table.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 메뉴가 포함된 주문 추가")
    @Test
    void create_Fail_With_NotExistMenu() {
        OrderLineItem orderLineItem = OrderLineItem.builder()
            .menuId(100L)
            .quantity(2)
            .build();
        Order order = Order.builder()
            .orderTableId(table.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderLineItems(Arrays.asList(orderLineItem))
            .orderedTime(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderLineItem orderLineItem = OrderLineItem.builder()
            .menuId(menu.getId())
            .quantity(2)
            .build();
        Order order = Order.builder()
            .orderTableId(100L)
            .orderStatus(OrderStatus.COOKING.name())
            .orderLineItems(Arrays.asList(orderLineItem))
            .orderedTime(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 주문 추가")
    @Test
    void create_Fail_With_EmptyTable() {
        OrderTable emptyTable = OrderTable.builder()
            .empty(true)
            .build();
        emptyTable = tableDao.save(emptyTable);

        OrderLineItem orderLineItem = OrderLineItem.builder()
            .menuId(menu.getId())
            .quantity(2)
            .build();
        Order order = Order.builder()
            .orderTableId(emptyTable.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderLineItems(Arrays.asList(orderLineItem))
            .orderedTime(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        orderService.create(order);
        orderService.create(order);

        List<Order> list = orderService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        Order create = orderService.create(this.order);
        Order target = Order.builder()
            .orderStatus(OrderStatus.COMPLETION.name())
            .build();

        Order changeOrderStatus = orderService.changeOrderStatus(create.getId(), target);

        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo(target.getOrderStatus());
    }
}