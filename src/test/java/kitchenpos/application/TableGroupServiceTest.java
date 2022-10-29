
package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.request.OrderTableGroupRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    private Long tableGroupId;
    private Long orderTableId1;
    private Long orderTableId2;

    @BeforeEach
    void setUp() {
        orderTableDao.save(new OrderTable(null, null, 0, true));
        orderTableDao.save(new OrderTable(null, null, 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, null, 0, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, null, 0, true));
        tableGroupId = tableGroupDao.save(new TableGroup(List.of(orderTable1, orderTable2))).getId();
        orderTableId1 = orderTable1.getId();
        orderTableId2 = orderTable2.getId();
    }

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
                List.of(new OrderTableGroupRequest(3L), new OrderTableGroupRequest(4L)));

        final TableGroupResponse response = tableGroupService.create(request);
        final List<OrderTableResponse> actualOrderTables = response.getOrderTables();

        assertAll(
                () -> assertThat(actualOrderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(actualOrderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        tableGroupService.ungroup(tableGroupId);
        final List<OrderTable> actualOrderTables = orderTableDao.findAllByIdIn(List.of(orderTableId1, orderTableId2));

        assertAll(
                () -> assertThat(actualOrderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(actualOrderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(actualOrderTables.get(1).getTableGroupId()).isNull(),
                () -> assertThat(actualOrderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("각 주문 테이블의 주문 상태가 식사 상태면 예외 발생")
    void whenIsMeal() {
        orderDao.save(new Order(orderTableId1, MEAL.name(), LocalDateTime.now(), List.of(new OrderLineItem(1L, 2))));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("각 주문 테이블의 주문 상태가 조리 상태면 예외 발생")
    void whenIsCooking() {
        orderDao.save(new Order(orderTableId2, COOKING.name(), LocalDateTime.now(), List.of(new OrderLineItem(1L, 2))));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
