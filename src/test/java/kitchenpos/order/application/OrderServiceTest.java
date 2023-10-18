package kitchenpos.order.application;

import static kitchenpos.fixture.OrderFixture.getOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderQueryResponse;
import kitchenpos.order.application.dto.OrderStatusModifyRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.persistence.OrderTableDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceIntegrateTest {


  @Autowired
  private OrderTableDao orderTableDao;
  @Autowired
  private OrderService orderService;

  private Long notEmptyOrderTableId;
  private Long emptyOrderTableId;

  @BeforeEach
  void init() {
    notEmptyOrderTableId = 1L;
    emptyOrderTableId = 2L;
    final OrderTable orderTable = orderTableDao.findById(notEmptyOrderTableId).get();
    orderTableDao.save(new OrderTable(orderTable.getId(), orderTable.getTableGroupId(),
        orderTable.getNumberOfGuests(), false));
  }

  @Test
  @DisplayName("주문을 등록할 수 있다.")
  void create_success() {
    //given, when
    final OrderQueryResponse actual = orderService.create(getOrderRequest(notEmptyOrderTableId));

    //then
    Assertions.assertAll(
        () -> assertThat(actual).isNotNull(),
        () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
    );

  }

  @Test
  @DisplayName("주문을 등록할 때 메뉴가 1개도 포함되어 있지 않다면 예외를 반환한다.")
  void create_fail_empty_orderLineItem() {
    //given
    final OrderCreateRequest order = getEmptyOrderRequest(notEmptyOrderTableId);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  private OrderCreateRequest getEmptyOrderRequest(final Long orderTableId) {
    return new OrderCreateRequest(orderTableId, Collections.emptyList());
  }

  @Test
  @DisplayName("주문을 등록할 때 존재하지 않는 메뉴가 포함되어 있으면 예외를 반환한다.")
  void create_fail_not_exist_menu() {
    //given, when
    final ThrowingCallable actual = () -> orderService.create(getWrongOrderRequest());

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  private OrderCreateRequest getWrongOrderRequest() {
    final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
        999L,
        1);
    return new OrderCreateRequest(1L, List.of(orderLineItemCreateRequest));
  }

  @Test
  @DisplayName("주문을 등록할 때 존재하지 않는 테이블의 주문이면 예외를 반환한다.")
  void create_fail_not_exist_table() {
    //given
    final OrderCreateRequest order = getOrderRequest(999L);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주문을 등록할 때 주문한 테이블이 비어있으면 예외를 반환한다.")
  void create_fail_empty_table() {
    //given
    final OrderCreateRequest order = getOrderRequest(emptyOrderTableId);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록된 주문 목록을 조회할 수 있다.")
  void list_success() {
    //given
    orderService.create(getOrderRequest(notEmptyOrderTableId));

    // when
    final List<OrderQueryResponse> actual = orderService.list();

    //then
    assertThat(actual).hasSize(1);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 수 있다.")
  void changeOrderStatus_success() {
    //given
    final OrderStatus newStatus = OrderStatus.MEAL;
    final OrderStatusModifyRequest order = new OrderStatusModifyRequest(newStatus);
    final Long savedOrderId = orderService.create(getOrderRequest(notEmptyOrderTableId)).getId();

    // when
    final OrderQueryResponse actual = orderService.changeOrderStatus(savedOrderId, order);

    //then
    assertThat(actual.getOrderStatus()).isEqualTo(newStatus.name());
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 때 존재하지 않는 주문이라면 예외를 반환한다.")
  void changeOrderStatus_fail_not_exist_order() {
    //given
    final OrderStatus newStatus = OrderStatus.MEAL;
    final OrderStatusModifyRequest order = new OrderStatusModifyRequest(newStatus);

    // when
    final ThrowingCallable actual = () -> orderService.changeOrderStatus(999L, order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 때 이미 계산이 완료된 주문이라면 예외를 반환한다.")
  void changeOrderStatus_fail_already_COMPLETEION() {
    //given
    final OrderStatus newStatus = OrderStatus.COMPLETION;
    final OrderStatusModifyRequest order = new OrderStatusModifyRequest(newStatus);
    final Long savedOrderId = orderService.create(getOrderRequest(notEmptyOrderTableId)).getId();

    orderService.changeOrderStatus(savedOrderId, order);

    // when
    final ThrowingCallable actual = () -> orderService.changeOrderStatus(savedOrderId, order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

}
