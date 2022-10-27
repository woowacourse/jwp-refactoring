package kitchenpos.domain.repository;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class OrderRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 주문을_저장하면_id가_채워진다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order = 주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null);

        Order savedOrder = orderRepository.save(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(order)
        );
    }

    @Test
    void 저장하는_주문의_id가_null이_아니면_업데이트_한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Long orderId = orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null))
                .getId();
        Order updateOrder = 주문을_생성한다(orderTableId, MEAL.name(), LocalDateTime.now(), null);
        updateOrder.setId(orderId);

        Order savedOrder = orderRepository.save(updateOrder);

        assertThat(savedOrder).usingRecursiveComparison()
                .isEqualTo(updateOrder);
    }

    @Test
    void id로_주문을_조회할_수_있다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order = orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null));

        Order actual = orderRepository.findById(order.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(order);
    }

    @Test
    void 없는_id로_주문을_조회하면_Optional_empty를_반환한다() {
        Optional<Order> actual = orderRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_주문을_조회할_수_있다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order1 = orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null));
        Order order2 = orderRepository.save(주문을_생성한다(orderTableId, MEAL.name(), LocalDateTime.now(), null));

        List<Order> actual = orderRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(order1, order2);
    }

    @Test
    void 주문_테이블에_해당하고_주문_상태_목록에_있는_주문이_있으면_true를_반환한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null));

        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING.name()));

        assertThat(actual).isTrue();
    }

    @Test
    void 주문_테이블에_해당하고_주문_상태_목록에_있는_주문이_없으면_false를_반환한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();

        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING.name()));

        assertThat(actual).isFalse();
    }

    @Test
    void 주문_테이블_목록에_있으면서_주문_상태_목록에_있는_주문이_있으면_true를_반환한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null));

        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTableId),
                List.of(COOKING.name()));

        assertThat(actual).isTrue();
    }

    @Test
    void 주문_테이블_목록에_있으면서_주문_상태_목록에_있는_주문이_없으면_false를_반환한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();

        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTableId),
                List.of(COOKING.name()));

        assertThat(actual).isFalse();
    }
}
