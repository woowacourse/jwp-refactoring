package kitchenpos;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@DisplayName("OrderService 테스트")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuService menuService;
    @Autowired
    private OrderTableDao tableDao;

    private Order order;
    private MenuGroup menuGroup;
    private Product product;
    private MenuProduct menuProduct;
    private Menu menu;
    private OrderTable table;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.builder()
                .name("이달의 메뉴")
                .build();
        menuGroup = menuGroupDao.save(menuGroup);

        product = Product.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(18_900))
                .build();
        product = productDao.save(product);

        menuProduct = MenuProduct.builder()
                .productId(product.getId())
                .quantity(1)
                .build();
        menu = Menu.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(18_900))
                .menuGroupId(menuGroup.getId())
                .menuProducts(Arrays.asList(menuProduct))
                .build();
        menu = menuService.create(menu);

        table = OrderTable.builder()
                .empty(false)
                .build();
        table = tableDao.save(table);

        orderLineItem = OrderLineItem.builder()
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
        Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
    }

    @DisplayName("주문 추가 - 실패 - 주문 항목이 없는 경우")
    @Test
    void createFailureWhenNoOrderLineItem() {
        Order order = Order.builder()
                .orderTableId(table.getId())
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 추가 - 실패 - 존재하지 않는 메뉴가 포함된 경우")
    @Test
    void createFailureWhenNotExistMenu() {
        OrderLineItem orderLineItem = OrderLineItem.builder()
                .menuId(999L)
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

    @DisplayName("주문 추가 - 실패 - 존재하지 않는 테이블인 경우")
    @Test
    void createFailureWhenNotExistTable() {
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

    @DisplayName("주문 추가 - 실패 - 빈 테이블인 경우")
    @Test
    void createFailureWhenEmptyTable() {
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
        assertThat(list).isNotEmpty();
    }

    @DisplayName("주문 상태 변경 - 성공")
    @Test
    void changeOrderStatus() {
        Order savedOrder = orderService.create(order);
        Order target = Order.builder()
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();
        Order changeOrderStatus = orderService.changeOrderStatus(savedOrder.getId(), target);

        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo(target.getOrderStatus());
    }

    @DisplayName("주문 상태 변경 - 실패 - 이미 완료된 주문인 경우")
    @Test
    void changeOrderStatus_With_CompletedOrder() {
        Order savedOrder = orderService.create(order);
        Order target = Order.builder()
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();
        orderService.changeOrderStatus(savedOrder.getId(), target);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), target))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
