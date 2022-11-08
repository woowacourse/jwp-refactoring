package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ServiceTest;
import kitchenpos.menu.application.request.TableGroupRequest;
import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;

public class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        TableGroupRequest request = createTableGroupRequest();

        // when
        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블이 빈 경우 예외를 던진다.")
    void create_empty_table() {
        // given
        TableGroupRequest request = createTableGroupRequest(List.of());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("order tables is empty or under size");
    }

    @Test
    @DisplayName("주문 테이블크기가 2 미만인 경우 예외를 던진다.")
    void create_table_under_size2() {
        // given
        OrderTable orderTable = new OrderTable(1, true);
        Long orderTableId = orderTableDao.save(orderTable).getId();
        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTableId, NO_ID, 1, true);
        TableGroupRequest request = createTableGroupRequest(List.of(orderTableRequest));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("order tables is empty or under size");
    }

    @Test
    @DisplayName("테이블 그룹에 속한 OrderTable 을 삭제한다.")
    void ungroup() {
        // given
        TableGroupRequest request = createTableGroupRequest();

        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        // when
        Long tableGroupId = savedTableGroup.getId();
        tableGroupService.ungroup(tableGroupId);

        // then
        assertThat(orderTableDao.findAllByTableGroupId(tableGroupId)).isEmpty();
    }

    private TableGroupRequest createTableGroupRequest() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        Long orderTableId1 = orderTableDao.save(orderTable1).getId();
        Long orderTableId2 = orderTableDao.save(orderTable2).getId();
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(orderTableId1, NO_ID, 1, true);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(orderTableId2, NO_ID, 2, true);
        return createTableGroupRequest(List.of(orderTableRequest1, orderTableRequest2));
    }

    private TableGroupRequest createTableGroupRequest(List<OrderTableRequest> tables) {
        return new TableGroupRequest(tables);
    }
}
