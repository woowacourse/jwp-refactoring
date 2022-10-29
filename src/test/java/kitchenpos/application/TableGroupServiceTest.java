
package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableGroupRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTableGroup() {
        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(new OrderTableGroupRequest(1L), new OrderTableGroupRequest(2L)));
        final TableGroupResponse actual = tableGroupService.create(request);

        for (OrderTableResponse orderTable : actual.getOrderTables()) {
            assertThat(actual.getId()).isEqualTo(orderTable.getTableGroupId());
        }
    }

    @Test
    @DisplayName("주문 테이블의 상태가 주문 테이블로 변경된다.")
    void createTableGroupIsChangeOrderTableStatus() {
        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(new OrderTableGroupRequest(1L), new OrderTableGroupRequest(2L)));

        final TableGroupResponse response = tableGroupService.create(request);
        final List<OrderTableResponse> actualOrderTables = response.getOrderTables();

        assertAll(
                () -> assertThat(actualOrderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(actualOrderTables.get(1).isEmpty()).isFalse()
        );
    }
    
//
//    @Test
//    @DisplayName("테이블 그룹을 해제한다.")
//    void ungroup() {
//        final TableGroup tableGroup = tableGroupDao.findById(주문상태_완료된_두번째테이블그룹)
//                .orElseThrow();
//        final List<Long> orderTableIds = orderTableDao.findAllByTableGroupId(tableGroup.getId())
//                .stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//
//        tableGroupService.ungroup(tableGroup.getId());
//        final List<OrderTable> actualOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
//
//        assertAll(
//                () -> assertThat(actualOrderTables.get(0).getTableGroupId()).isNull(),
//                () -> assertThat(actualOrderTables.get(0).isEmpty()).isFalse(),
//                () -> assertThat(actualOrderTables.get(1).getTableGroupId()).isNull(),
//                () -> assertThat(actualOrderTables.get(1).isEmpty()).isFalse()
//        );
//    }
//
//    @Test
//    @DisplayName("각 주문 테이블의 주문 상태가 완료상태가 아니면 예외 발생")
//    void whenIsNotCompletion() {
//        final TableGroup tableGroup = tableGroupDao.findById(주문상태_안료되지_않은_첫번째테이블그룹)
//                .orElseThrow();
//
//        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
}
