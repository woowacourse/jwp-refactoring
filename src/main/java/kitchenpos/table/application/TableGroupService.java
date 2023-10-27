package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.table.application.mapper.TableGroupMapper;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.SingleOrderTableCreateRequest;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
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
        final TableGroup tableGroup = TableGroupMapper.toTableGroup(LocalDateTime.now(), orderTables);
        tableGroupValidator.validate(tableGroup);

        return tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> convertToOrderTables(final List<SingleOrderTableCreateRequest> requests) {
        return requests.stream()
                .map(this::convertToOrderTable)
                .collect(Collectors.toList());
    }

    private OrderTable convertToOrderTable(final SingleOrderTableCreateRequest request) {
        return orderTableRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 order table 입니다."));
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 table group 입니다."));
        tableGroupValidator.validateOrderStatus(tableGroup);

        tableGroup.ungroup();
    }

}
