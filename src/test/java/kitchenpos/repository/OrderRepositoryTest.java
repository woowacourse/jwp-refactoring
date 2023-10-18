package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Nested
    class findByOrderTableId {

        @Test
        void 주문_테이블_식별자에_대한_주문이_없으면_empty() {
            // given
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, false, 0));
            Order order = new Order(null, OrderStatus.COMPLETION, orderedTime, orderTable);
            orderRepository.save(order);

            //when
            Optional<Order> actual = orderRepository.findByOrderTableId(4885L);

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문_테이블_식별자에_대한_주문이_있으면_present() {
            // given
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, false, 0));
            Order order = new Order(null, OrderStatus.COMPLETION, orderedTime, orderTable);
            orderRepository.save(order);

            //when
            Optional<Order> actual = orderRepository.findByOrderTableId(orderTable.getId());

            // then
            assertThat(actual).isPresent();
        }
    }

    @Nested
    class existsByOrderTableIdInAndOrderStatusIn {

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 주문_테이블_식별자_목록이_포함되고_주문_상태가_포함되면_true(OrderStatus status) {
            // given
            List<OrderStatus> orderStatues = List.of(OrderStatus.COOKING, OrderStatus.MEAL);

            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, false, 0));
            List<Long> orderTableIds = List.of(4885L, 4886L, orderTable.getId());

            Order order = new Order(null, status, orderedTime, orderTable);
            orderRepository.save(order);

            // when
            boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatues);

            // then
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 주문_테이블_식별자_목록에_포함이_되지_않고_주문_상태가_포함되면_false(OrderStatus status) {
            // given
            List<Long> orderTableIds = List.of(4885L, 4886L);
            List<OrderStatus> orderStatues = List.of(OrderStatus.COOKING, OrderStatus.MEAL);

            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, false, 0));
            Order order = new Order(null, status, orderedTime, orderTable);
            orderRepository.save(order);

            // when
            boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatues);

            // then
            assertThat(actual).isFalse();
        }

        @Test
        void 주문_테이블_식별자_목록에_포함되고_주문_상태가_포함되지_않으면_false() {
            // given
            List<OrderStatus> orderStatues = List.of(OrderStatus.COOKING, OrderStatus.MEAL);

            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, false, 0));
            List<Long> orderTableIds = List.of(4885L, 4886L, orderTable.getId());

            Order order = new Order(null, OrderStatus.COMPLETION, orderedTime, orderTable);
            orderRepository.save(order);

            // when
            boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatues);

            // then
            assertThat(actual).isFalse();
        }
    }
}
