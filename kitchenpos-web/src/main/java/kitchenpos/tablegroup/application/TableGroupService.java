package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.mapper.TableGroupMapper;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.SingleOrderTableCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final TableGroup tableGroup = saveTableGroup(tableGroupCreateRequest);

        return TableGroupMapper.toTableGroupResponse(tableGroup);
    }

    private TableGroup saveTableGroup(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<OrderTable> orderTables = convertToOrderTables(tableGroupCreateRequest.getOrderTables());
        final TableGroup tableGroup = TableGroupMapper.toTableGroup();
        tableGroupValidator.validate(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup.getId()));
        return savedTableGroup;
    }

    private List<OrderTable> convertToOrderTables(final List<SingleOrderTableCreateRequest> requests) {
        return requests.stream()
                .map(SingleOrderTableCreateRequest::getId)
                .map(id -> orderTableRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테이블 입니다."))
                ).collect(Collectors.toList());
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        tableGroupValidator.validateOrderStatus(orderTables);
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(null));
    }
}
