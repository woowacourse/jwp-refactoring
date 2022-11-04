package kitchenpos.domain.order;

import static kitchenpos.domain.common.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.RepositoryTest;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 주문을_저장하면_id가_채워진다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItem orderLineItem = new OrderLineItem(1, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        Order order = new Order(orderTableId, List.of(orderLineItem), false);

        Order savedOrder = orderRepository.save(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(order)
        );
    }

    @Test
    void id로_주문을_조회할_수_있다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItem orderLineItem = new OrderLineItem(1, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        Order order = orderRepository
                .save(new Order(orderTableId, List.of(orderLineItem), false));

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
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItem orderLineItem1 = new OrderLineItem(1, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        OrderLineItem orderLineItem2 = new OrderLineItem(1, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        Order order1 = orderRepository.save(new Order(orderTableId, List.of(orderLineItem1), false));
        Order order2 = orderRepository.save(new Order(orderTableId, List.of(orderLineItem2), false));

        List<Order> actual = orderRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(order1, order2);
    }

    @Test
    void 주문_테이블에_해당하고_주문_상태_목록에_있는_주문이_있으면_true를_반환한다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItem orderLineItem = new OrderLineItem(1, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        orderRepository.save(new Order(orderTableId, List.of(orderLineItem), false));

        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING));

        assertThat(actual).isTrue();
    }

    @Test
    void 주문_테이블에_해당하고_주문_상태_목록에_있는_주문이_없으면_false를_반환한다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();

        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING));

        assertThat(actual).isFalse();
    }

    @Test
    void 주문_테이블_목록에_있으면서_주문_상태_목록에_있는_주문이_있으면_true를_반환한다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();
        OrderLineItem orderLineItem = new OrderLineItem(1, new OrderedMenu("메뉴 이름", new Price(BigDecimal.ZERO)));
        orderRepository.save(new Order(orderTableId, List.of(orderLineItem), false));

        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTableId),
                List.of(COOKING));

        assertThat(actual).isTrue();
    }

    @Test
    void 주문_테이블_목록에_있으면서_주문_상태_목록에_있는_주문이_없으면_false를_반환한다() {
        Long orderTableId = orderTableRepository.save(new OrderTable(1, false))
                .getId();

        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTableId),
                List.of(COOKING));

        assertThat(actual).isFalse();
    }
}
