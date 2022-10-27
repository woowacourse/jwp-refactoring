package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        TableGroupCreateRequest request = createTableGroupRequest();

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
        TableGroupCreateRequest request = createTableGroupRequest();

        TableGroup savedTableGroup = tableGroupService.create(request);

        // when
        Long tableGroupId = savedTableGroup.getId();
        tableGroupService.ungroup(tableGroupId);

        // then
        assertThat(orderTableDao.findAllByTableGroupId(tableGroupId)).isEmpty();
    }

    private TableGroupCreateRequest createTableGroupRequest() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        Long orderTableId1 = orderTableDao.save(orderTable1).getId();
        Long orderTableId2 = orderTableDao.save(orderTable2).getId();
        OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(orderTableId1);
        OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(orderTableId2);
        return createTableGroupRequest(List.of(orderTableCreateRequest1, orderTableCreateRequest2));
    }

    private TableGroupCreateRequest createTableGroupRequest(List<OrderTableCreateRequest> tables) {
        return new TableGroupCreateRequest(tables);
    }
}
