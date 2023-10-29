package kitchenpos.order.domain;

import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;

import domain.Order;
import domain.OrderStatus;
import domain.OrderTable;
import exception.OrderException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문_상태를_변경한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);
        Order 주문 = new Order();

        주문.changeOrderStatus("MEAL");

        Assertions.assertThat(주문.getOrderStatus() == OrderStatus.MEAL);

    }

    @Test
    void 완료된_주문의_상태를_변경할_수_없다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);
        Order 주문 = new Order();

        주문.changeOrderStatus("COMPLETION");

        Assertions.assertThatThrownBy(() -> 주문.changeOrderStatus("MEAL"))
                .isInstanceOf(OrderException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

    @ValueSource(strings = {"MEAL", "COOKING"})
    @ParameterizedTest
    void 주문_상태가_주문_완료인지_확인한다(String orderStatus) {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);
        Order 주문 = new Order();

        주문.changeOrderStatus(orderStatus);

        Assertions.assertThatThrownBy(() -> 주문.validateOrderStatusIsCompletion())
                .isInstanceOf(OrderException.class)
                .hasMessage("주문 상태가 주문 완료가 아닙니다.");
    }
}
