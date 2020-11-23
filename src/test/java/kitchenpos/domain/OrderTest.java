package kitchenpos.domain;

import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderTest {

    @DisplayName("Order 생성, OrderTable이 null일 때 예외 반환")
    @Test
    void wrongCreate() {
        assertThatNullPointerException()
                .isThrownBy(() -> Order.cooking(null))
                .withMessage("OrderTable null이면 Order를 생성할 수 없습니다.");
    }

    @DisplayName("Order 생성, 비어있는 OrderTable일 때 예외 반환")
    @Test
    void wrongCreate2() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Order.cooking(createOrderTableWithEmpty(true)))
                .withMessage("비어있는 order table에 order를 생성할 수 없습니다.");
    }

    @DisplayName("orderStatus 정상 작동")
    @Test
    void changeOrderStatus() {
        Order cooking = Order.cooking(createOrderTableWithEmpty(false));

        Order actual = cooking.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("Order 원래 상태가 COMPLETION 일 때 상태 바꾸기 예외 반환")
    @Test
    void changeWrongOrderStatus() {
        Order order = OrderFixture.createOrderWithOrderStatus(OrderStatus.COMPLETION);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .withMessage("완료된 주문은 상태를 변경할 수 없습니다.");
    }
}