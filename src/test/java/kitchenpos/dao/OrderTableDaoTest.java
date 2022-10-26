package kitchenpos.dao;

import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableDaoTest extends JdbcDaoTest {

    @Test
    void 주문테이블을_저장할_수_있다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable orderTable = ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId);

        // when
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    void 주문테이블_아이디로_조회한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId));

        // when
        final OrderTable orderTable = orderTableDao.findById(savedOrderTable.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void 아이디_목록으로_존재하는_주문테이블을_조회할_수_있다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId));
        final long notExistOrderTableId = Long.MAX_VALUE;

        // when
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(
                List.of(savedOrderTable.getId(), notExistOrderTableId));

        // then
        assertThat(orderTables).usingFieldByFieldElementComparator()
                .hasSize(1)
                .containsOnly(savedOrderTable);
    }
    
    @Test
    void 테이블그룹으로_주문테이블_목록을_조회한다() {
        // given
        final Long tableGroupId = 테이블그룹을_저장한다(TABLE_GROUP_NOW.생성()).getId();
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성(tableGroupId));

        // when
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).usingFieldByFieldElementComparator()
                .hasSize(1)
                .containsOnly(savedOrderTable);
    }
}
