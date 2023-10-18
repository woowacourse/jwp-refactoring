package kitchenpos.application;


import static kitchenpos.fixture.OrderTableFixture.단체_지정이_없는_주문_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.단체_지정이_있는_주문_테이블_생성;
import static kitchenpos.fixture.TableGroupFixture.빈_테이블_그룹_생성;
import static kitchenpos.fixture.TableGroupFixture.오더_테이블이_있는_테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 주문_테이블이_null_이면_저장에_실패한다() {
        // given
        TableGroup tableGroup = 빈_테이블_그룹_생성();

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_1개_이하이면_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(List.of(savedOrderTable));

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_주문_테이블_이면_저장에_실패한다() {
        // given
        List<OrderTable> savedOrderTables = List.of(
                단체_지정이_없는_주문_테이블_생성(1, true),
                단체_지정이_없는_주문_테이블_생성(1, true)
        );
        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_중_하나라도_주문이_가능한_상태이면_저장에_실패한다() {
        // given
        List<OrderTable> savedOrderTables = List.of(
                orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, false)),
                orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true))
        );
        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_중_하나라도_이미_단체_지정에_속해있으면_저장에_실패한다() {
        // given
        TableGroup savedTableGroup = tableGroupDao.save(빈_테이블_그룹_생성());
        List<OrderTable> savedOrderTables = List.of(
                orderTableDao.save(단체_지정이_있는_주문_테이블_생성(savedTableGroup, 1, false)),
                orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true))
        );

        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // expect
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 성공적으로_단체_지정을_저장한다() {
        // given
        OrderTable orderTable1 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        OrderTable orderTable2 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));

        List<OrderTable> savedOrderTables = List.of(
                orderTable1,
                orderTable2
        );
        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);

        // when
        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        // then
        TableGroup savedTableGroup = tableGroupDao.findById(savedTableGroupId).get();
        OrderTable savedOrderTable1 = orderTableDao.findById(orderTable1.getId()).get();
        OrderTable savedOrderTable2 = orderTableDao.findById(orderTable2.getId()).get();
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedOrderTable1.getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
                () -> assertThat(savedOrderTable2.getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse()
        );
    }

    @Test
    void 단체_지정을_삭제할_때_연관된_주문_중_현재_요리_중인_것이_있으면_안된다() {
        // given
        OrderTable orderTable1 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        주문_테이블에_원하는_상태의_주문을_추가한다(orderTable1, OrderStatus.COOKING);
        OrderTable orderTable2 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        List<OrderTable> savedOrderTables = List.of(
                orderTable1,
                orderTable2
        );
        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);
        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        // expect
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_삭제할_때_연관된_주문_중_현재_식사_중인_것이_있으면_안된다() {
        // given
        OrderTable orderTable1 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        주문_테이블에_원하는_상태의_주문을_추가한다(orderTable1, OrderStatus.MEAL);
        OrderTable orderTable2 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        List<OrderTable> savedOrderTables = List.of(
                orderTable1,
                orderTable2
        );
        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);
        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        // expect
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 주문_테이블에_원하는_상태의_주문을_추가한다(OrderTable orderTable, OrderStatus orderStatus) {
        orderTable.setEmpty(false);
        orderTableDao.save(orderTable);

        Order savedOrder = 주문을_저장하고_반환받는다(orderTable);
        주문의_상태를_변환한다(savedOrder, orderStatus);

        orderTable.setEmpty(true);
        orderTableDao.save(orderTable);
    }

    @Test
    void 주문_테이블을_성공적으로_삭제해준다() {
        // given
        OrderTable orderTable1 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        OrderTable orderTable2 = orderTableDao.save(단체_지정이_없는_주문_테이블_생성(1, true));
        List<OrderTable> savedOrderTables = List.of(
                orderTable1,
                orderTable2
        );
        TableGroup tableGroup = 오더_테이블이_있는_테이블_그룹_생성(savedOrderTables);
        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        // when
        tableGroupService.ungroup(savedTableGroupId);
        OrderTable savedOrderTable1 = orderTableDao.findById(orderTable1.getId()).get();
        OrderTable savedOrderTable2 = orderTableDao.findById(orderTable2.getId()).get();

        // then
        assertAll(
                () -> assertThat(savedOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
                () -> assertThat(savedOrderTable2.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable2.isEmpty()).isFalse()
        );
    }

}
