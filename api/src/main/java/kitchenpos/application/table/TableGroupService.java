package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.repository.TableGroupRepository;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.exception.NonExistentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableService orderTableService, TableGroupRepository tableGroupRepository) {
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.orderTableIds();
        TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());
        List<OrderTable> orderTables = orderTableService.findAllOrderTables(orderTableIds);
        tableGroup.updateOrderTables(orderTables);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        if (!tableGroupRepository.existsById(tableGroupId)) {
            throw new NonExistentException("요청한 TableGroup이 존재하지 않습니다.");
        }
        orderTableService.ungroup(tableGroupId);
    }
}
