package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemCreateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.dto.OrderStatusDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 항목이 빈 경우, 예외를 발생한다")
    @Test
    void empty_order_line_items_exception() {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, null, 1, false));
        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비었습니다.");
    }

    @DisplayName("주문 테이블이 없는 경우, 예외를 발생한다")
    @Test
    void does_not_exist_order_table_exception() {
        final OrderCreateRequest request = new OrderCreateRequest(null,
                Collections.singletonList(new OrderLineItemCreateRequest(1L, 1L)));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 없습니다.");
    }

    @DisplayName("주문 테이블이 빈 경우, 예외를 발생한다")
    @Test
    void empty_order_table_exception() {
        final OrderCreateRequest request = new OrderCreateRequest(1L,
                Collections.singletonList(new OrderLineItemCreateRequest(1L, 1L)));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비었습니다.");
    }

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        // given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, null, 1, false));

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(new OrderLineItemCreateRequest(1L, 1L)));

        // when
        final OrderResponse response = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(response.getOrderStatus()).isEqualToIgnoringCase(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문 전체 목록을 조회한다")
    @Test
    void findAll() {
        // given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, null, 1, false));

        final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(new OrderLineItemCreateRequest(1L, 1L)));

        orderService.create(request);

        // when
        final List<OrderResponse> responses = orderService.list();

        // then
        assertThat(responses).hasSize(1);
    }

    @DisplayName("주문 상태를 변경할 때 주문 번호가 없는 경우, 예외를 발생한다")
    @Test
    void not_found_order_exception() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusDto("MEAL")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경할 때 주문 상태가 completion인 경우, 예외를 발생한다")
    @Test
    void order_state_completion_exception() {
        // given
        final Order orderRequest = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L)));
        final Order order = orderDao.save(orderRequest);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new OrderStatusDto("MEAL")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문 상태는 변경할 수 없습니다.");
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void change_order_status() {
        // given
        final Order orderRequest = new Order(1L, OrderStatus.COOKING, LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L)));
        final Order order = orderDao.save(orderRequest);
        final OrderStatusDto changeOrderStatus = new OrderStatusDto("MEAL");

        // when
        final OrderStatusDto response = orderService.changeOrderStatus(order.getId(), changeOrderStatus);

        // then
        assertThat(response.getOrderStatus()).isEqualTo(changeOrderStatus.getOrderStatus());
    }
}
