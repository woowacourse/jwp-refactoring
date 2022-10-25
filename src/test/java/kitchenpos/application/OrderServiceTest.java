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
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class OrderServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_생성할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of(orderLineItem));

        Order savedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isNotNull(),
                () -> assertThat(orderLineItem.getOrderId()).isEqualTo(savedOrder.getId())
        );
    }

    @Test
    void 생성하려는_주문에_주문_항목이_없으면_예외를_반환한다() {
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order = 주문을_생성한다(0L, null, LocalDateTime.now(), List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_빈_주문_테이블이면_예외를_반환한다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order = 주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_주문_목록을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order1 = orderService.create(주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of(orderLineItem)));
        Order order2 = orderService.create(주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of(orderLineItem)));

        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(2)
                .usingElementComparatorIgnoringFields("orderLineItems")
                .containsExactly(order1, order2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order = orderService.create(주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of(orderLineItem)));
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
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, false))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        Order order = orderService.create(주문을_생성한다(orderTableId, null, LocalDateTime.now(), List.of(orderLineItem)));
        order.setOrderStatus(COMPLETION.name());
        orderService.changeOrderStatus(order.getId(), order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
