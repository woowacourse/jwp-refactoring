package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.exception.NonExistentException;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
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
        List<OrderTable> savedOrderTables = orderTableService.findAllOrderTables(orderTableIds);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());
        List<OrderTable> orderTables = orderTableService.create(savedOrderTables, savedTableGroup);
        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        if (!tableGroupRepository.existsById(tableGroupId)) {
            throw new NonExistentException("요청한 TableGroup이 존재하지 않습니다.");
        }
        orderTableService.ungroup(tableGroupId);
    }
}
