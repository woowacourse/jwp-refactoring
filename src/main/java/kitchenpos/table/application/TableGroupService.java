package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.request.TableGroupTableRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        List<OrderTable> orderTables = findOrderTables(request.getOrderTables());

        tableGroup.setUpOrderTable(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));

        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> findOrderTables(List<TableGroupTableRequest> orderTableRequests) {
        List<Long> orderTableIds = orderTableRequests.stream()
            .map(TableGroupTableRequest::getId)
            .collect(Collectors.toList());
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTablesSize(orderTableRequests, orderTables);
        return orderTables;
    }

    private void validateOrderTablesSize(List<TableGroupTableRequest> orderTableRequests,
                                         List<OrderTable> orderTables) {
        if (orderTableRequests.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블입니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findByIdOrThrow(tableGroupId);
        tableGroup.ungroup();
    }
}
