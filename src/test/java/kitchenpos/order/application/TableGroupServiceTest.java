package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ServiceTest;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.menu.application.request.TableGroupRequest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.application.request.OrderTableRequest;

public class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        TableGroupRequest request = createTableGroupRequest();

        // when
        TableGroup savedTableGroup = tableGroupService.create(request);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹에 속한 OrderTable 을 삭제한다.")
    void ungroup() {
        // given
        TableGroupRequest request = createTableGroupRequest();

        TableGroup savedTableGroup = tableGroupService.create(request);

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
