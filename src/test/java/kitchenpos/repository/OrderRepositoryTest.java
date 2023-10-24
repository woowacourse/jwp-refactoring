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
    class findAllByOrderTableIdIn {

        @Test
        void 식별자_목록으로_모든_엔티티_조회() {
            // given
            for (int i = 0; i < 5; i++) {
                LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
                OrderTable orderTable = orderTableRepository.save(new OrderTable(null, false, 0));
                Order order = new Order(null, OrderStatus.COOKING, createdDate, orderTable);
                orderRepository.save(order);
            }

            // when
            List<Order> actual = orderRepository.findAllByOrderTableIdIn(List.of(1L, 2L, 3L, 4L, 5L));

            // then
            assertThat(actual).hasSize(5);
        }
    }
}
