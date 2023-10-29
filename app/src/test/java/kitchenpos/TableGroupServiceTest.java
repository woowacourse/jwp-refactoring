package kitchenpos;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import kitchenpos.table.TableGroup;
import kitchenpos.table.TableGroupRepository;
import kitchenpos.table.TableGroupService;
import kitchenpos.table.ui.TableGroupRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_단체지정 {
        @Test
        void 테이블들을_단체_지정한다() {
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

            TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId()));
            TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);


            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @Test
        void 단체_지정하려는_테이블이_2개_미만이면_지정할_수_없다() {
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 1, true));

            TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(orderTable.getId()));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정 테이블의 개수는 2개 이상이어야 합니다.");
        }

        @Test
        void 존재하지_않는_테이블을_단체로_지정할_수_없다() {
            OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 1, true));
            Long notExistOrderTableId = Long.MIN_VALUE;

            TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(savedOrderTable.getId(), notExistOrderTableId));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블을 지정했습니다. 단체 지정 할 수 없습니다.");
        }

        @Test
        void 비어있지_않은_테이블을_단체로_지정할_수_없다() {
            OrderTable emptyTable = orderTableRepository.save(new OrderTable(1, true));
            OrderTable notEmptyTable = orderTableRepository.save(new OrderTable(2, false));

            TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(emptyTable.getId(), notEmptyTable.getId()));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않은 테이블이거나 이미 단체지정된 테이블입니다. 단체 지정할 수 없습니다.");
        }

        @Test
        void 이미_지정된_테이블을_단체_지정할_수_없다() {
            OrderTable alreadyGrouped1 = orderTableRepository.save(new OrderTable(null, 1, true));
            OrderTable alreadyGrouped2 = orderTableRepository.save(new OrderTable(null, 2, true));
            tableGroupService.create(new TableGroupRequest(List.of(alreadyGrouped1.getId(), alreadyGrouped2.getId())));

            TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(alreadyGrouped1.getId(), alreadyGrouped2.getId()));

            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않은 테이블이거나 이미 단체지정된 테이블입니다. 단체 지정할 수 없습니다.");
        }
    }

    @Test
    void 단체_지정을_삭제한다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId())));

        tableGroupService.ungroup(tableGroup.getId());

        assertSoftly(softly -> {
            softly.assertThat(orderTable1.getTableGroupId()).isNull();
            softly.assertThat(orderTable2.getTableGroupId()).isNull();
        });
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 단체_지정된_주문_테이블의_주문_상태가_조리_또는_식사인_경우_단체_지정을_삭제할_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));
        TableGroup tableGroup = tableGroupService.create(new TableGroupRequest(List.of(orderTable1.getId(), orderTable2.getId())));


        orderRepository.save(new Order(orderTable1.getId(), orderStatus.name(), LocalDateTime.now(), List.of()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문이 이미 조리 혹은 식사 중입니다. 단체 지정을 삭제할 수 없습니다.");
    }
}
