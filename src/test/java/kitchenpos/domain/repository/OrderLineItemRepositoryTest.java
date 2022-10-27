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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class OrderLineItemRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupDao;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Test
    void 주문_항목을_저장하면_id가_채워진다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Order order = orderRepository.save(
                주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), new ArrayList<>()));
        OrderLineItem orderLineItem = 주문_항목을_생성한다(order, menuId, 1);

        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);

        assertAll(
                () -> assertThat(savedOrderLineItem.getSeq()).isNotNull(),
                () -> assertThat(savedOrderLineItem).usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(orderLineItem)
        );
    }

    @Test
    void id로_주문_항목을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Order order = orderRepository.save(
                주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), new ArrayList<>()));
        OrderLineItem orderLineItem = orderLineItemRepository.save(주문_항목을_생성한다(order, menuId, 1));

        OrderLineItem actual = orderLineItemRepository.findById(orderLineItem.getSeq())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderLineItem);
    }

    @Test
    void 없는_id로_주문_항목을_조회하면_Optional_empty를_반환한다() {
        Optional<OrderLineItem> actual = orderLineItemRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_주문_항목을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Order order = orderRepository.save(
                주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), new ArrayList<>()));
        OrderLineItem orderLineItem1 = orderLineItemRepository.save(주문_항목을_생성한다(order, menuId, 1));
        OrderLineItem orderLineItem2 = orderLineItemRepository.save(주문_항목을_생성한다(order, menuId, 2));

        List<OrderLineItem> actual = orderLineItemRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderLineItem1, orderLineItem2);
    }

    @Test
    void 주문에_포함된_주문_항목을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, new ArrayList<>()))
                .getId();
        Long orderTableId = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Order order1 = orderRepository.save(
                주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), new ArrayList<>()));
        Order order2 = orderRepository.save(
                주문을_생성한다(orderTableId, MEAL.name(), LocalDateTime.now(), new ArrayList<>()));
        OrderLineItem orderLineItem = orderLineItemRepository.save(주문_항목을_생성한다(order1, menuId, 1));
        orderLineItemRepository.save(주문_항목을_생성한다(order2, menuId, 1));

        List<OrderLineItem> actual = orderLineItemRepository.findAllByOrder(order1);

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderLineItem);
    }
}
