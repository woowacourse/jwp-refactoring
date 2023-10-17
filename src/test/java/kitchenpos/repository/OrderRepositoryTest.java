package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
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

    @Nested
    class findByOrderTableId {

        @Test
        void 주문_테이블_식별자에_대한_주문이_없으면_empty() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);
            order.setOrderStatus(OrderStatus.COMPLETION);
            order.setOrderedTime(LocalDateTime.parse("2023-10-15T22:40:00"));
            orderRepository.save(order);

            //when
            Optional<Order> actual = orderRepository.findByOrderTableId(4885L);

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문_테이블_식별자에_대한_주문이_있으면_present() {
            // given
            Order order = new Order();
            order.setOrderTableId(4885L);
            order.setOrderStatus(OrderStatus.COMPLETION);
            order.setOrderedTime(LocalDateTime.parse("2023-10-15T22:40:00"));
            orderRepository.save(order);

            //when
            Optional<Order> actual = orderRepository.findByOrderTableId(4885L);

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
            List<Long> orderTableIds = List.of(4885L, 4886L);
            List<OrderStatus> orderStatues = List.of(OrderStatus.COOKING, OrderStatus.MEAL);

            Order order = new Order();
            order.setOrderTableId(4885L);
            order.setOrderStatus(status);
            order.setOrderedTime(LocalDateTime.parse("2023-10-15T22:40:00"));
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

            Order order = new Order();
            order.setOrderTableId(4884L);
            order.setOrderStatus(status);
            order.setOrderedTime(LocalDateTime.parse("2023-10-15T22:40:00"));
            orderRepository.save(order);

            // when
            boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatues);

            // then
            assertThat(actual).isFalse();
        }

        @ParameterizedTest
        @ValueSource(longs = {4885, 4886})
        void 주문_테이블_식별자_목록에_포함되고_주문_상태가_포함되지_않으면_false(Long orderTableId) {
            // given
            List<Long> orderTableIds = List.of(4885L, 4886L);
            List<OrderStatus> orderStatues = List.of(OrderStatus.COOKING, OrderStatus.MEAL);

            Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderStatus(OrderStatus.COMPLETION);
            order.setOrderedTime(LocalDateTime.parse("2023-10-15T22:40:00"));
            orderRepository.save(order);

            // when
            boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatues);

            // then
            assertThat(actual).isFalse();
        }
    }
}
