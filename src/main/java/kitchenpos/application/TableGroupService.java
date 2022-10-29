package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableIdRequest;
import kitchenpos.application.dto.request.TableGroupsCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.exception.InvalidOrderTableToGroupException;
import kitchenpos.exception.TableGroupNotfoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupsCreateRequest tableGroupsCreateRequest) {
        final List<Long> orderTablesId = mapToOrderTableIds(tableGroupsCreateRequest);
        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTablesId);
        validateDuplicatedOrderTableId(orderTablesId, orderTables);
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    private static List<Long> mapToOrderTableIds(final TableGroupsCreateRequest tableGroupsCreateRequest) {
        return tableGroupsCreateRequest.getOrderTables().stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private static void validateDuplicatedOrderTableId(final List<Long> orderTablesId, final List<OrderTable> orderTables) {
        if (orderTablesId.size() != orderTables.size()) {
            throw new InvalidOrderTableToGroupException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotfoundException::new);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }
}
