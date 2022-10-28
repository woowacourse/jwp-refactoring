package kitchenpos.dao;

import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.OrderFixture.ORDER_COOKING_1;
import static kitchenpos.support.OrderLineItemFixture.ORDER_LINE_ITEM_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderDaoTest extends JdbcDaoTest {

    @Test
    void 주문을_저장한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId)).getId();
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Long menuId = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId)).getId();
        final Order order = ORDER_COOKING_1.생성(orderTableId, List.of(ORDER_LINE_ITEM_1.생성(menuId)));

        // when
        final Order savedOrder = orderDao.save(order);

        // then
        assertThat(savedOrder.getId()).isEqualTo(1L);
    }

    @Test
    void 주문을_아이디로_조회한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId)).getId();
        final LocalDateTime localDateTime = LocalDateTime.of(2022, 10, 24, 10, 10);
        final Order savedOrder = 주문을_저장한다(ORDER_COOKING_1.주문항목_없이_생성(orderTableId, localDateTime));

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
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId)).getId();
        final Order savedOrder = 주문을_저장한다(ORDER_COOKING_1.주문항목_없이_생성(orderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));

        // when
        final List<Order> orders = orderDao.findAll();

        // then
        assertThat(orders).usingRecursiveFieldByFieldElementComparator()
                .containsOnly(savedOrder);
    }

    @Test
    void 특정_주문상태를_갖는_주문테이블이_존재하는지_확인한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long orderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId)).getId();
        주문을_저장한다(ORDER_COOKING_1.주문항목_없이_생성(orderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));

        // when
        final boolean exist = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of("COOKING", "MEAL"));

        // then
        assertThat(exist).isTrue();
    }

    @Test
    void 특정_주문상태를_갖는_주문테이블들이_존재하는지_확인한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final Long firstOrderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId)).getId();
        주문을_저장한다(ORDER_COOKING_1.주문항목_없이_생성(firstOrderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));
        final Long secondOrderTableId = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId)).getId();
        주문을_저장한다(ORDER_COOKING_1.주문항목_없이_생성(secondOrderTableId, LocalDateTime.of(2022, 10, 24, 10, 10)));

        // when
        final boolean exist = orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(firstOrderTableId, secondOrderTableId), List.of("COOKING", "MEAL"));

        // then
        assertThat(exist).isTrue();
    }
}
