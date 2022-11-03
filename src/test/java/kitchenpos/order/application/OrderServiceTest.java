package kitchenpos.order.application;

import static kitchenpos.order.fixture.OrderFixture.createOrder;
import static kitchenpos.table.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.exception.NotFoundException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.request.OrderCreateRequest;
import kitchenpos.order.ui.dto.request.OrderLineItemDto;
import kitchenpos.order.ui.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.ui.dto.response.OrderCreateResponse;
import kitchenpos.order.ui.dto.response.OrderFindAllResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문을 생성한다.")
    @Test
    void create_success() {
        // given
        OrderTable orderTable = orderTableRepository.save(createOrderTable());
        OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.singletonList(
                new OrderLineItemDto(1L, 1)));

        // when
        OrderCreateResponse response = orderService.create(request);

        // then
        Order dbOrder = orderRepository.findById(response.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(dbOrder.getOrderTableId()).isEqualTo(request.getOrderTableId());
    }

    @DisplayName("주문을 생성할 때 존재하지 않는 메뉴라면 예외를 반환한다.")
    @Test
    void create_fail_if_not_exist_menu() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable());
        OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.singletonList(
                new OrderLineItemDto(9999999L, 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("주문을 생성할 때 존재하지 않는 주문테이블이라면 예외를 반환한다.")
    @Test
    void create_fail_if_not_exist_orderTable() {
        // given
        OrderCreateRequest request = new OrderCreateRequest(9999999L, Collections.singletonList(
                new OrderLineItemDto(1L, 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("주문을 생성할 때 주문항목 내 메뉴ID가 중복되어있다면 예외를 반환한다.")
    @Test
    void create_fail_if_menu_id_duplicate() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable());
        OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Arrays.asList(
                new OrderLineItemDto(1L, 1),
                new OrderLineItemDto(1L, 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("주문을 생성할 때 주문테이블이 비어있다면 예외를 반환한다.")
    @Test
    void create_fail_if_orderTable_is_empty() {
        // given
        OrderTable orderTable = orderTableRepository.save(createOrderTable(true));
        OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(new OrderLineItemDto(1L, 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        OrderTable orderTable = orderTableRepository.save(createOrderTable());
        Order order = orderRepository.save(
                createOrder(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now()));

        // when
        List<OrderFindAllResponse> responses = orderService.list();

        // then
        List<Long> orderIds = responses.stream()
                .map(OrderFindAllResponse::getId)
                .collect(Collectors.toList());
        assertThat(orderIds).contains(order.getId());
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus_success() {
        // given
        OrderTable orderTable = orderTableRepository.save(createOrderTable());
        OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.singletonList(
                new OrderLineItemDto(1L, 1)));
        OrderCreateResponse response = orderService.create(request);

        // when
        OrderStatus changedOrderStatus = OrderStatus.MEAL;
        orderService.changeOrderStatus(response.getId(), new OrderStatusChangeRequest(changedOrderStatus.name()));

        // then
        Order dbOrder = orderRepository.findById(response.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(dbOrder.getOrderStatus()).isEqualTo(changedOrderStatus);
    }

    @DisplayName("기존 주문 상태가 COMPLETION인 주문을 수정하려고하면 예외를 발생한다.")
    @Test
    void changeOrderStatus_fail_if_exist_orderStatus_is_COMPLETION() {
        // given
        OrderTable orderTable = orderTableRepository.save(createOrderTable());
        Order order = createOrder(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(),
                new OrderStatusChangeRequest(OrderStatus.COOKING.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
