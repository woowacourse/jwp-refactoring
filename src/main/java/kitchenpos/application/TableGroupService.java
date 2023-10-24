package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.request.TableGroupTableRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
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

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findByIdOrThrow(tableGroupId);
        tableGroup.ungroup();
    }
}
