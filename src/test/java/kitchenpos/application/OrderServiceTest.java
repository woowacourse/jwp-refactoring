package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest(classes = {
        TableGroupRepository.class,
        MenuGroupRepository.class,
        MenuRepository.class,
        OrderRepository.class,
        OrderLineItemRepository.class,
        OrderTableRepository.class,
        OrderService.class
})
class OrderServiceTest extends ServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderService orderService;

    private TableGroup tableGroup;

    private OrderTable orderTable;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        tableGroup = tableGroupRepository.save(createTableGroup(Collections.emptyList()));
        orderTable = orderTableRepository.save(createOrderTable(tableGroup.getId(), 5, false));

        final MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("이십마리메뉴"));
        final Menu menu = menuRepository.save(createMenu("후라이드치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.emptyList()));
        orderLineItem = createOrderLineItem(menu.getId(), 1L);
    }

    @DisplayName("create: 주문 생성")
    @Test
    void create() {
        final Order order = createOrder(orderTable.getId(), null, Collections.singletonList(orderLineItem));
        final Order actual = orderService.create(order);

        assertThat(actual).isNotNull();
        assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(actual.getOrderLineItems()).isNotEmpty();
    }

    @DisplayName("create: 주문 항목이 비어있을 때 예외 처리")
    @Test
    void create_IfOrderLineItemEmpty_Exception() {
        final Order order = createOrder(orderTable.getId(), null, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 항목의 메뉴와 메뉴에서 조회한 것이 다를 때 예외 처리")
    @Test
    void create_IfOrderLineItemNotSameMenus_Exception() {
        orderLineItem.setMenuId(0L);
        final Order order = createOrder(orderTable.getId(), null, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블이 없을 때 예외 처리")
    @Test
    void create_IfOrderTableDoesNotExist_Exception() {
        final Order order = createOrder(0L, null, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블이 비어 있을 때 예외 처리")
    @Test
    void create_IfOrderTableEmpty_Exception() {
        final OrderTable emptyOrderTable = orderTableRepository.save(createOrderTable(tableGroup.getId(), 5, true));
        final Order order = createOrder(emptyOrderTable.getId(), null, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 주문 전체 조회")
    @Test
    void list() {
        final Order order = createOrder(orderTable.getId(), null, Collections.singletonList(orderLineItem));
        orderService.create(order);

        final List<Order> orders = orderService.list();

        assertThat(orders).isNotEmpty();
    }

    @DisplayName("changeOrderStatus: 주문 상태를 변경")
    @Test
    void changeOrderStatus() {
        final Order order = orderService.create(createOrder(orderTable.getId(), null, Collections.singletonList(orderLineItem)));
        final Order orderStatusMeal = createOrder(null, "MEAL", null);

        final Order actual = orderService.changeOrderStatus(order.getId(), orderStatusMeal);

        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("changeOrderStatus: 주문을 찾을 수 없을 때 예외 처리")
    @Test
    void changeOrderStatus_IfNotFindId_Exception() {
        final Order orderStatusMeal = createOrder(null, "MEAL", null);

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, orderStatusMeal))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeOrderStatus: 주문 상태가 이미 완료되어 있을 때 예외 처리")
    @Test
    void changeOrderStatus_IfOrderStatusIsCompletion_Exception() {
        final Order order = orderService.create(createOrder(orderTable.getId(), null, Collections.singletonList(orderLineItem)));
        final Order orderStatusMeal = createOrder(null, "COMPLETION", null);
        orderService.changeOrderStatus(order.getId(), orderStatusMeal);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderStatusMeal))
                .isInstanceOf(IllegalArgumentException.class);
    }
}