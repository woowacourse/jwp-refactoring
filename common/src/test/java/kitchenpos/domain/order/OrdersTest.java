package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdersTest {

    private static Stream<Arguments> orderList() {
        Order order_that_status_is_meal = new Order(
                new OrderTable(10, false),
                OrderStatus.MEAL.name()
        );

        Order order_that_status_is_cooking = new Order(
                new OrderTable(10, false),
                OrderStatus.COOKING.name()
        );

        Order order_that_status_is_completion = new Order(
                new OrderTable(10, false),
                OrderStatus.COMPLETION.name()
        );
        return Stream.of(
                Arguments.of(
                        Arrays.asList(order_that_status_is_cooking, order_that_status_is_completion),
                        Arrays.asList(order_that_status_is_meal, order_that_status_is_completion),
                        Arrays.asList(order_that_status_is_meal, order_that_status_is_cooking)
                )
        );
    }

    @DisplayName("주문리스트에 포함된 주문이 모두 완료되었는지 검증한다.")
    @ParameterizedTest(name = "{displayName} - 실패, cooking 또는 meal 상태가 포함됨.")
    @MethodSource("orderList")
    void checkNotCompleted(List<Order> orderList) {
        // given
        Orders orders = new Orders(orderList);

        // when - then
        assertThatThrownBy(orders::checkNotCompleted)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아직 조리 혹은 식사 중인 주문이 존재합니다.");
    }
}
