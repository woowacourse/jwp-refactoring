package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_항목을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class OrderServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_생성할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), new ArrayList<>());
        OrderLineItem orderLineItem = 주문_항목을_생성한다(order, menuId, 1);

        Order savedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isNotNull(),
                () -> assertThat(orderLineItem.getOrder()).isEqualTo(savedOrder)
        );
    }

    @Test
    void 생성하려는_주문에_주문_항목이_없으면_예외를_반환한다() {
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Order order = 주문을_생성한다(0L, null, LocalDateTime.now(), new ArrayList<>());
        주문_항목을_생성한다(order, menuId, 1);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_빈_주문_테이블이면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), new ArrayList<>());
        주문_항목을_생성한다(order, menuId, 1);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_주문_목록을_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order1 = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), new ArrayList<>());
        Order order2 = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), new ArrayList<>());
        주문_항목을_생성한다(order1, menuId, 1);
        주문_항목을_생성한다(order2, menuId, 1);
        orderService.create(order1);
        orderService.create(order2);

        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(2)
                .usingElementComparatorIgnoringFields("orderLineItems")
                .containsExactly(order1, order2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), new ArrayList<>());
        OrderLineItem orderLineItem = 주문_항목을_생성한다(order, menuId, 1);
        orderService.create(order);
        order.setOrderStatus(MEAL.name());

        Order changedOrder = orderService.changeOrderStatus(order.getId(), order);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    void 상태를_변경하려는_주문이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태를_변경하려는_주문이_완료된_주문이면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), new ArrayList<>());
        주문_항목을_생성한다(order, menuId, 1);
        orderService.create(order);
        order.setOrderStatus(COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
