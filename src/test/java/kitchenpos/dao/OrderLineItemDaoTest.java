package kitchenpos.dao;

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
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class OrderLineItemDaoTest {

    @Autowired
    private MenuGroupRepository menuGroupDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Test
    void 주문_항목을_저장하면_id가_채워진다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Long orderId = orderDao.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(orderId, menuId, 1);

        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

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
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Long orderId = orderDao.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null))
                .getId();
        OrderLineItem orderLineItem = orderLineItemDao.save(주문_항목을_생성한다(orderId, menuId, 1));

        OrderLineItem actual = orderLineItemDao.findById(orderLineItem.getSeq())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderLineItem);
    }

    @Test
    void 없는_id로_주문_항목을_조회하면_Optional_empty를_반환한다() {
        Optional<OrderLineItem> actual = orderLineItemDao.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_주문_항목을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Long orderId = orderDao.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null))
                .getId();
        OrderLineItem orderLineItem1 = orderLineItemDao.save(주문_항목을_생성한다(orderId, menuId, 1));
        OrderLineItem orderLineItem2 = orderLineItemDao.save(주문_항목을_생성한다(orderId, menuId, 2));

        List<OrderLineItem> actual = orderLineItemDao.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderLineItem1, orderLineItem2);
    }

    @Test
    void 주문에_포함된_주문_항목을_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuDao.save(메뉴를_생성한다("메뉴", BigDecimal.valueOf(1_000), menuGroupId, null))
                .getId();
        Long orderTableId = orderTableDao.save(주문_테이블을_생성한다(null, 1, true))
                .getId();
        Long orderId1 = orderDao.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null))
                .getId();
        Long orderId2 = orderDao.save(주문을_생성한다(orderTableId, MEAL.name(), LocalDateTime.now(), null))
                .getId();
        OrderLineItem orderLineItem = orderLineItemDao.save(주문_항목을_생성한다(orderId1, menuId, 1));
        orderLineItemDao.save(주문_항목을_생성한다(orderId2, menuId, 1));

        List<OrderLineItem> actual = orderLineItemDao.findAllByOrderId(orderId1);

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(orderLineItem);
    }
}
