package kitchenpos.domain;

import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 빈_테이블을_등록할_수_없다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 0, true);
        assertThatThrownBy(() -> new Order(주문_테이블, List.of(새로운_주문_항목(null, 3))))
                .isInstanceOf(OrderException.class)
                .hasMessage("빈 테이블을 등록할 수 없습니다.");
    }

    @Test
    void 주문_항목이_있어야_한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);
        assertThatThrownBy(() -> new Order(주문_테이블, List.of()))
                .isInstanceOf(OrderException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    void 주문_상태를_변경한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);
        Order 주문 = new Order(주문_테이블, List.of(새로운_주문_항목(null, 3)));

        주문.changeOrderStatus("MEAL");

        assertThat(주문.getOrderStatus() == OrderStatus.MEAL);

    }

    @Test
    void 완료된_주문의_상태를_변경할_수_없다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);
        Order 주문 = new Order(주문_테이블, List.of(새로운_주문_항목(null, 3)));

        주문.changeOrderStatus("COMPLETION");

        assertThatThrownBy(() -> 주문.changeOrderStatus("MEAL"))
                .isInstanceOf(OrderException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

    @ValueSource(strings = {"MEAL", "COOKING"})
    @ParameterizedTest
    void 주문_상태가_주문_완료인지_확인한다(String orderStatus) {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);
        Order 주문 = new Order(주문_테이블, List.of(새로운_주문_항목(null, 3)));

        주문.changeOrderStatus(orderStatus);

        assertThatThrownBy(() -> 주문.validateOrderStatusIsCompletion())
                .isInstanceOf(OrderException.class)
                .hasMessage("주문 상태가 주문 완료가 아닙니다.");
    }
}
