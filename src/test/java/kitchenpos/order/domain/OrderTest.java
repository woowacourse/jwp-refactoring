package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

class OrderTest {

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        final Order order = new Order(OrderStatus.MEAL, LocalDateTime.now());

        // when
        final OrderStatus orderStatus = OrderStatus.COMPLETION;
        order.changeOrderStatus(orderStatus);

        // then
        assertThat(order.orderStatus()).isEqualTo(orderStatus);
    }

    @Test
    @DisplayName("주문 테이블 그룹을 해제한다.")
    void ungroupOrderTable() {
        // given
        final Order order = new Order(OrderStatus.COMPLETION, LocalDateTime.now());
        ReflectionTestUtils.setField(order, "orderTable", new OrderTable(3, false));

        // when, then
        assertThatNoException()
                .isThrownBy(order::ungroupOrderTable);
    }

    @ParameterizedTest
    @CsvSource(value = {"COMPLETION,false", "MEAL,true"})
    @DisplayName("주문 상태가 요리 혹은 식사 중인지 확인한다.")
    void isStatusCookingOrMeal(final OrderStatus orderStatus, final boolean expect) {
        // given
        final Order order = new Order(orderStatus, LocalDateTime.now());

        // when
        final boolean isStatusCookingOrMeal = order.isStatusCookingOrMeal();

        // then
        assertThat(isStatusCookingOrMeal).isEqualTo(expect);
    }
}
