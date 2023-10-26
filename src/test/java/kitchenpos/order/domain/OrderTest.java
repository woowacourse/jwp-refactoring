package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@Import(OrderValidator.class)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class))
class OrderTest {

    @Autowired
    private OrderValidator orderValidator;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void throw_when_try_to_change_completed_order_status() {
        // given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = new Order(
                1L,
                savedOrderTable.getId(),
                OrderStatus.COMPLETION,
                LocalDateTime.now(),
                List.of(),
                orderValidator
        );

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order already completed cannot be changed");
    }

    @Test
    void change_order_status() {
        // given
        final OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = new Order(
                1L,
                savedOrderTable.getId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                List.of(),
                orderValidator
        );

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}
