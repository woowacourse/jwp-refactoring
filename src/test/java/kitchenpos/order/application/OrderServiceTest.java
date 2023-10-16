package kitchenpos.order.application;

import static kitchenpos.fixture.OrderFixture.빈_주문;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderFixture.주문_잘못된_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.order.domain.Order;
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
    final Order actual = orderService.create(주문(notEmptyOrderTableId));

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
    final Order order = 빈_주문(notEmptyOrderTableId);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주문을 등록할 때 존재하지 않는 메뉴가 포함되어 있으면 예외를 반환한다.")
  void create_fail_not_exist_menu() {
    //given, when
    final ThrowingCallable actual = () -> orderService.create(주문_잘못된_메뉴());

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주문을 등록할 때 존재하지 않는 테이블의 주문이면 예외를 반환한다.")
  void create_fail_not_exist_table() {
    //given
    final Order order = 주문(999L);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("주문을 등록할 때 주문한 테이블이 비어있으면 예외를 반환한다.")
  void create_fail_empty_table() {
    //given
    final Order order = 주문(emptyOrderTableId);

    //when
    final ThrowingCallable actual = () -> orderService.create(order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록된 주문 목록을 조회할 수 있다.")
  void list_success() {
    //given
    orderService.create(주문(notEmptyOrderTableId));

    // when
    final List<Order> actual = orderService.list();

    //then
    assertThat(actual).hasSize(1);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 수 있다.")
  void changeOrderStatus_success() {
    //given
    final String newStatus = OrderStatus.MEAL.name();
    final Order order = 주문(notEmptyOrderTableId, newStatus);
    final Long savedOrderId = orderService.create(order).getId();

    // when
    final Order actual = orderService.changeOrderStatus(savedOrderId, order);

    //then
    assertThat(actual.getOrderStatus()).isEqualTo(newStatus);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 때 존재하지 않는 주문이라면 예외를 반환한다.")
  void changeOrderStatus_fail_not_exist_order() {
    //given
    final String newStatus = OrderStatus.MEAL.name();
    final Order order = 주문(notEmptyOrderTableId, newStatus);

    // when
    final ThrowingCallable actual = () -> orderService.changeOrderStatus(999L, order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("등록된 주문 상태를 수정할 때 이미 계산이 완료된 주문이라면 예외를 반환한다.")
  void changeOrderStatus_fail_already_COMPLETEION() {
    //given
    final String newStatus = OrderStatus.COMPLETION.name();
    final Order order = 주문(notEmptyOrderTableId, newStatus);
    final Long savedOrderId = orderService.create(order).getId();

    orderService.changeOrderStatus(savedOrderId, order);

    // when
    final ThrowingCallable actual = () -> orderService.changeOrderStatus(savedOrderId, order);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

}
