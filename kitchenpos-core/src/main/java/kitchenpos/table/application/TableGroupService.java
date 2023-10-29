package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.domain.service.TableGroupValidator;
import kitchenpos.table.domain.service.TableUngroupValidator;
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
    private final TableGroupValidator tableGroupValidator;
    private final TableUngroupValidator tableUngroupValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator, TableUngroupValidator tableUngroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.tableUngroupValidator = tableUngroupValidator;
    }

    public TableGroupResponse group(final TableGroupCreateRequest request) {
        List<OrderTable> orderTables = findOrderTables(request.getOrderTables());
        TableGroup tableGroup = TableGroup.create(orderTables, tableGroupValidator);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> findOrderTables(List<TableGroupTableRequest> orderTableRequests) {
        List<Long> orderTableIds = orderTableRequests.stream()
            .map(TableGroupTableRequest::getId)
            .collect(Collectors.toList());
        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        validateOrderTablesSize(orderTableRequests, savedOrderTables);
        return savedOrderTables;
    }

    private void validateOrderTablesSize(List<TableGroupTableRequest> orderTableRequests,
                                         List<OrderTable> orderTables) {
        if (orderTableRequests.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블입니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findByIdOrThrow(tableGroupId);
        tableGroup.ungroup(tableUngroupValidator);
    }
}
