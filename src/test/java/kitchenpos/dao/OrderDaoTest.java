package kitchenpos.dao;

import static kitchenpos.support.OrderFixture.ORDER_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Test;

class OrderDaoTest extends JdbcDaoTest {

    @Test
    void 주문을_저장한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_1.생성(tableGroupId)).getId();
        final Order order = ORDER_1.생성(orderTableId);

        // when
        final Order savedOrder = orderDao.save(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void 주문을_아이디로_조회한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_1.생성(tableGroupId)).getId();
        final Order savedOrder = 주문을_저장한다(ORDER_1.생성(orderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));

        // when
        final Order foundOrder = orderDao.findById(savedOrder.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(foundOrder.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(foundOrder.getOrderedTime()).isEqualTo(LocalDateTime.of(2022, 10, 24, 10, 10))
        );
    }

    @Test
    void 모든_주문을_조회한다() {
        // given
        final int alreadyExistCount = orderDao.findAll()
                .size();
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_1.생성(tableGroupId)).getId();
        final Order savedOrder = 주문을_저장한다(ORDER_1.생성(orderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));

        // when
        final List<Order> orders = orderDao.findAll();

        // then
        assertThat(orders).usingRecursiveFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedOrder);
    }

    @Test
    void 특정_주문상태를_갖는_주문테이블이_존재하는지_확인한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_1.생성(tableGroupId)).getId();
        주문을_저장한다(ORDER_1.생성(orderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));

        // when
        final boolean exist = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of("COOKING", "MEAL"));

        // then
        assertThat(exist).isTrue();
    }

    @Test
    void 특정_주문상태를_갖는_주문테이블들이_존재하는지_확인한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long firstOrderTableId = 주문테이블을_저장한다(ORDER_TABLE_1.생성(tableGroupId)).getId();
        주문을_저장한다(ORDER_1.생성(firstOrderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));
        final Long secondOrderTableId = 주문테이블을_저장한다(ORDER_TABLE_1.생성(tableGroupId)).getId();
        주문을_저장한다(ORDER_1.생성(secondOrderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));

        // when
        final boolean exist = orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(firstOrderTableId, secondOrderTableId), List.of("COOKING", "MEAL"));

        // then
        assertThat(exist).isTrue();
    }
}
