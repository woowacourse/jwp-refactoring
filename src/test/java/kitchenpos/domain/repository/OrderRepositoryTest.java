package kitchenpos.domain.repository;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_항목을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class OrderRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 주문을_저장하면_id가_채워진다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order = 주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), List.of(orderLineItem));

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
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order = orderRepository
                .save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), List.of(orderLineItem)));

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
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem1 = 주문_항목을_생성한다(null, menuId, 1);
        OrderLineItem orderLineItem2 = 주문_항목을_생성한다(null, menuId, 1);
        Order order1 = orderRepository.save(
                주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), List.of(orderLineItem1)));
        Order order2 = orderRepository.save(
                주문을_생성한다(orderTableId, MEAL.name(), LocalDateTime.now(), List.of(orderLineItem2)));

        List<Order> actual = orderRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(order1, order2);
    }

    @Test
    void 주문_테이블에_해당하고_주문_상태_목록에_있는_주문이_있으면_true를_반환한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), List.of(orderLineItem)));

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
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), List.of(orderLineItem)));

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
