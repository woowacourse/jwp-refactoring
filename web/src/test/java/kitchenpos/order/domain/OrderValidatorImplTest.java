package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.OrderValidatorImpl;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.support.DataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class OrderValidatorImplTest {

    @Autowired
    private DataCleaner dataCleaner;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderValidatorImpl orderValidator;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("주문상태가 완료이면 예외가 발생하지 않는다.")
    @Test
    void validate_order_status_is_completion() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5));

        orderRepository.save(new Order(1L, orderTable1.getId(), OrderStatus.COMPLETION));
        orderRepository.save(new Order(2L, orderTable1.getId(), OrderStatus.COMPLETION));

        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
        // when
        // then
        assertDoesNotThrow(() -> orderValidator.validateOrderStatusInCookingOrMeal(orderTables));
    }

    @DisplayName("주문상태가 완료가 아니면 예외가 발생한다.")
    @MethodSource("getOrderStatus")
    @ParameterizedTest
    void validate_order_status_is_not_completion(final OrderStatus orderStatus1, final OrderStatus orderStatus2) {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5));

        orderRepository.save(new Order(1L, orderTable1.getId(), orderStatus1));
        orderRepository.save(new Order(2L, orderTable1.getId(), orderStatus2));

        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
        // when
        // then
        assertThatThrownBy(() -> orderValidator.validateOrderStatusInCookingOrMeal(orderTables))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("현재 요리중이거나 식사 중인 경우 그룹해제를 할 수 없습니다.");
    }

    private static Stream<Arguments> getOrderStatus() {
        return Stream.of(
            Arguments.of(OrderStatus.COOKING, OrderStatus.COMPLETION),
            Arguments.of(OrderStatus.MEAL, OrderStatus.COMPLETION)
        );
    }
}
