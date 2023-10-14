package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void OrderTables가_null_이면_안된다() {
        // given
        TableGroup tableGroup = TableGroupFixture.빈_테이블_그룹_생성();

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTables가_1개_이하이면_안된다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true));
        TableGroup tableGroup = TableGroupFixture.오더_테이블이_있는_테이블_그룹_생성(List.of(savedOrderTable));

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_OrderTables이며_안된다() {
        // given
        List<OrderTable> savedOrderTables = List.of(
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true),
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true)
        );
        TableGroup tableGroup = TableGroupFixture.오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTables중_하나라도_주문이_가능한_상태이면_안된다() {
        // given
        List<OrderTable> savedOrderTables = List.of(
                orderTableDao.save(OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false)),
                orderTableDao.save(OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true))
        );
        TableGroup tableGroup = TableGroupFixture.오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTables중_하나라도_이미_TableGroup에_속해있으면_안된다() {
        // given
        TableGroup savedTableGroup = tableGroupDao.save(TableGroupFixture.빈_테이블_그룹_생성());
        List<OrderTable> savedOrderTables = List.of(
                orderTableDao.save(OrderTableFixture.테이블_그룹이_있는_주문_테이블_생성(savedTableGroup, 1, false)),
                orderTableDao.save(OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true))
        );

        TableGroup tableGroup = TableGroupFixture.오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 성공적으로_TableGroup을_저장한다() {
        // given
        OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, true));

        List<OrderTable> savedOrderTables = List.of(
                orderTable1,
                orderTable2
        );
        TableGroup tableGroup = TableGroupFixture.오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // when
        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        // then
        TableGroup savedTableGroup = tableGroupDao.findById(savedTableGroupId)
                .orElseThrow(NoSuchElementException::new);
        OrderTable savedOrderTable1 = orderTableDao.findById(orderTable1.getId())
                .orElseThrow(NoSuchElementException::new);
        OrderTable savedOrderTable2 = orderTableDao.findById(orderTable2.getId())
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedOrderTable1.getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
                () -> assertThat(savedOrderTable2.getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse()
        );
    }

}
