package kitchenpos.tablegroup.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableValidator tableValidator,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = getOrderTables(request);
        tableGroup.grouping(orderTables);
        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> getOrderTables(final TableGroupCreateRequest request) {
        return request.getOrderTables()
                .stream()
                .map(orderTableRepository::getById)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        tableGroup.validate((it) -> tableValidator.validatePossibleChangeToEmpty((Long) it));
        tableGroup.ungrouping();
    }
}
