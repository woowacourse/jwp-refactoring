package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeStatusRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.utils.TestFixture;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class OrderServiceTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("create: 주문 등록 테스트")
    @Test
    void createTest() {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        final Order order = orderService.create(orderCreateRequest);

        assertAll(
                () -> assertThat(order).isNotNull(),
                () -> assertThat(order.getOrderTableId()).isEqualTo(orderTable.getId())
        );
    }

    @DisplayName("create: 주문 등록 시 테이블이 비어있는 경우 예외처리")
    @Test
    void createTestByOrderTableEmpty() {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithEmpty());
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 전체 주문을 확인하는 테스트")
    @Test
    void listTest() {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        orderService.create(orderCreateRequest);

        final List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getId()).isNotNull()
        );
    }

    @DisplayName("changeOrderStatus: 주문 상태를 변경하는 테스트")
    @Test
    void changeOrderStatusTest() {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        final Order order = orderService.create(orderCreateRequest);

        final OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest("MEAL");

        final OrderResponse orderResponse = orderService.changeOrderStatus(order.getId(), orderChangeStatusRequest);

        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("changeOrderStatus: 주문 상태가 Completion일 경우 예외처리")
    @Test
    void changeOrderStatusTestByCompletionOfOrderState() {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        final Order order = orderService.create(orderCreateRequest);

        orderService.changeOrderStatus(order.getId(), new OrderChangeStatusRequest("COMPLETION"));
        final OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderChangeStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
