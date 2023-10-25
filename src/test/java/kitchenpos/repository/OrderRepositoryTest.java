package kitchenpos.repository;

import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.vo.NumberOfGuests;
import kitchenpos.repository.config.RepositoryTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문 테이블 id의 주문의 OrderStatus가 Completion인지 확인")
    @Nested
    class ExistsByOrderTableIdAndCompletion {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final OrderTable orderTable = new OrderTable(new NumberOfGuests(2), false);
            em.persist(orderTable);
            final Order order = new Order(OrderStatus.COMPLETION, orderTable);
            em.persist(order);

            // when
            final boolean actual = orderRepository.existsByOrderTableIdAndCompletion(orderTable.getId());

            // then
            assertThat(actual).isTrue();
        }

        @DisplayName("주문이 없으면 false를 반환한다.")
        @Test
        void false_when_no_order() {
            // given
            final OrderTable orderTable = new OrderTable(new NumberOfGuests(2), false);
            em.persist(orderTable);

            // when
            final boolean actual = orderRepository.existsByOrderTableIdAndCompletion(orderTable.getId());

            // then
            assertThat(actual).isFalse();
        }

        @DisplayName("주문 상태가 조리중이라면 false를 반환한다.")
        @Test
        void false_when_order_status_is_COOKING() {
            // given
            final OrderTable orderTable = new OrderTable(new NumberOfGuests(2), false);
            em.persist(orderTable);
            final Order order = new Order(OrderStatus.COOKING, orderTable);
            em.persist(order);

            // when
            final boolean actual = orderRepository.existsByOrderTableIdAndCompletion(orderTable.getId());

            // then
            assertThat(actual).isFalse();
        }
    }

    @DisplayName("주문 테이블 id 목록으로 계산된 주문 개수를 확인한다.")
    @Nested
    class CountCompletionOrderByOrderTableIds {
        @DisplayName("개수 세기에 성공한다.")
        @Test
        void success() {
            // given
            final List<Long> orderTableIds = new ArrayList<>();
            final long expected = 10;

            orderTableIds.add(saveOrderByOrderStatusAndReturnOrderTableId(OrderStatus.MEAL));
            for (int i = 0; i < expected; i++) {
                orderTableIds.add(saveOrderByOrderStatusAndReturnOrderTableId(OrderStatus.COMPLETION));
            }
            orderTableIds.add(saveOrderByOrderStatusAndReturnOrderTableId(OrderStatus.COOKING));

            // when
            final Long actual = orderRepository.countCompletionOrderByOrderTableIds(orderTableIds);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        private Long saveOrderByOrderStatusAndReturnOrderTableId(final OrderStatus orderStatus) {
            final OrderTable orderTable = new OrderTable(new NumberOfGuests(2), false);
            em.persist(orderTable);
            final Order order = new Order(orderStatus, orderTable);
            em.persist(order);
            return orderTable.getId();
        }
    }
}
