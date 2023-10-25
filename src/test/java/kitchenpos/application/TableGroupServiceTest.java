package kitchenpos.application;


import static kitchenpos.fixture.OrderTableFixture.단체_지정이_없는_주문_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.단체_지정이_있는_주문_테이블_생성;
import static kitchenpos.fixture.TableGroupFixture.빈_테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 주문_테이블이_null_이면_저장에_실패한다() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                null
        );

        // expect
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_1개_이하이면_저장에_실패한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(savedOrderTable.getId())
        );

        // expect
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_주문_테이블_이면_저장에_실패한다() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        Long.MAX_VALUE - 1,
                        Long.MAX_VALUE
                )
        );

        // expect
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_중_하나라도_주문이_가능한_상태이면_저장에_실패한다() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, false)).getId(),
                        orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true)).getId()
                )
        );

        // expect
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_중_하나라도_이미_단체_지정에_속해있으면_저장에_실패한다() {
        // given
        TableGroup savedTableGroup = tableGroupRepository.save(빈_테이블_그룹_생성());
        TableGroupCreateRequest request= new TableGroupCreateRequest(
                List.of(
                        orderTableRepository.save(
                                단체_지정이_있는_주문_테이블_생성(savedTableGroup, 1, true)
                        ).getId(),
                        orderTableRepository.save(
                                단체_지정이_없는_주문_테이블_생성(1, true)
                        ).getId()
                )
        );

        // expect
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 성공적으로_단체_지정을_저장한다() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        OrderTable orderTable2 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                orderTable1.getId(),
                orderTable2.getId()
        ));

        // when
        Long savedTableGroupId = tableGroupService.create(request).getId();

        // then
        TableGroup savedTableGroup = tableGroupRepository.findById(savedTableGroupId).get();
        OrderTable savedOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable savedOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();
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
        OrderTable orderTable1 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        주문_테이블에_원하는_상태의_주문을_추가한다(orderTable1, OrderStatus.COOKING);
        OrderTable orderTable2 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(orderTable1.getId(), orderTable2.getId())
        );
        Long savedTableGroupId = tableGroupService.create(request).getId();

        // expect
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_삭제할_때_연관된_주문_중_현재_식사_중인_것이_있으면_안된다() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        주문_테이블에_원하는_상태의_주문을_추가한다(orderTable1, OrderStatus.MEAL);
        OrderTable orderTable2 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(orderTable1.getId(), orderTable2.getId())
        );
        Long savedTableGroupId = tableGroupService.create(request).getId();

        // expect
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 주문_테이블에_원하는_상태의_주문을_추가한다(OrderTable orderTable, OrderStatus orderStatus) {
        orderTable.changeEmpty(false);
        orderTableRepository.save(orderTable);

        Order savedOrders = 주문을_저장하고_반환받는다(orderTable);
        주문의_상태를_변환한다(savedOrders, orderStatus);

        orderTable.changeEmpty(true);
        orderTableRepository.save(orderTable);
    }

    @Test
    void 주문_테이블을_성공적으로_삭제해준다() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        OrderTable orderTable2 = orderTableRepository.save(단체_지정이_없는_주문_테이블_생성(1, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(orderTable1.getId(), orderTable2.getId())
        );
        Long savedTableGroupId = tableGroupService.create(request).getId();

        // when
        tableGroupService.ungroup(savedTableGroupId);
        OrderTable savedOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable savedOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();

        // then
        assertAll(
                () -> assertThat(savedOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
                () -> assertThat(savedOrderTable2.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable2.isEmpty()).isFalse()
        );
    }

}
