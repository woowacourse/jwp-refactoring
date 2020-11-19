package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.utils.TestObjectFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.repository.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderTable orderedTable
            = tableService.create(createOrderTable(2, false));
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        Order order
            = TestObjectFactory.createOrder(orderedTable, null, orderLineItems);
        Order savedOrder = orderService.create(order);

        assertAll(() -> {
            assertThat(savedOrder).isInstanceOf(Order.class);
            assertThat(savedOrder.getId()).isNotNull();
            assertThat(savedOrder.getOrderTable()).isNotNull();
            assertThat(savedOrder.getOrderStatus()).isNotNull();
            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
            assertThat(savedOrder.getOrderLineItems()).isNotNull();
            assertThat(savedOrder.getOrderLineItems()).isNotEmpty();
        });
    }

    @DisplayName("주문을 생성한다. - 메뉴 리스트가 비어있을 경우")
    @Test
    void create_IfEmptyMenuList_ThrowException() {
        OrderTable orderedTable
            = tableService.create(createOrderTable(1, false));
        List<OrderLineItem> orderLineItems
            = Collections.emptyList();
        Order order
            = TestObjectFactory.createOrder(orderedTable, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다. - 주문 테이블이 존재하지 않을 경우")
    @Test
    void create_IfTableNotExist_ThrowException() {
        Menu menu = menuRepository.getOne(0L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        Order order
            = TestObjectFactory.createOrder(null, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다. - 주문 테이블이 비어있을 경우")
    @Test
    void create_IfTableEmpty_ThrowException() {
        OrderTable orderedTable
            = tableService.create(createOrderTable(1, true));
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        Order order
            = TestObjectFactory.createOrder(orderedTable, null, orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        OrderTable orderedTable
            = tableService.create(createOrderTable(1, false));
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        Order order
            = TestObjectFactory.createOrder(orderedTable, COOKING.name(), orderLineItems);
        orderService.create(order);

        List<Order> orders = orderService.list();

        assertAll(() -> {
            assertThat(orders).isNotEmpty();
            assertThat(orders).hasSize(1);
        });
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        OrderTable orderedTable
            = tableService.create(createOrderTable(1, false));
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        Order oldOrder
            = TestObjectFactory.createOrder(orderedTable, null, orderLineItems);
        Order newOrder
            = TestObjectFactory.createOrder(orderedTable, MEAL.name(), orderLineItems);

        Order savedOldOrder = orderService.create(oldOrder);
        orderService.changeOrderStatus(savedOldOrder.getId(), newOrder);
        Order savedNewOrder = orderService.list()
            .get(0);

        assertThat(savedNewOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("주문 상태를 변경한다. - 존재하지 않는 주문일 경우")
    @Test
    void changeOrderStatus_NotExistOrder_ThrowException() {
        OrderTable orderedTable
            = tableService.create(createOrderTable(1, false));
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        Order oldOrder
            = TestObjectFactory.createOrder(orderedTable, null, orderLineItems);
        Order newOrder
            = TestObjectFactory.createOrder(orderedTable, MEAL.name(), orderLineItems);

        orderService.create(oldOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, newOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다. - 계산 완료인 경우")
    @Test
    void changeOrderStatus_AlreadyPayed_ThrowException() {
        OrderTable orderedTable
            = tableService.create(createOrderTable(1, false));
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        Order oldOrder
            = TestObjectFactory
            .createOrder(orderedTable, null, orderLineItems);
        Order newOrder
            = TestObjectFactory
            .createOrder(orderedTable, COMPLETION.name(), orderLineItems);

        Order savedOldOrder = orderService.create(oldOrder);
        orderService.changeOrderStatus(savedOldOrder.getId(), newOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOldOrder.getId(), newOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
