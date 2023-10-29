package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupOrderTableValidator;
import kitchenpos.tablegroup.domain.TableGroupingService;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.tablegroup.presentation.dto.request.CreateTableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;

    private final TableGroupOrderTableValidator tableGroupOrderTableValidator;

    private final TableGroupingService tableGroupingService;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final TableGroupOrderTableValidator tableGroupOrderTableValidator,
                             final TableGroupingService tableGroupingService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupOrderTableValidator = tableGroupOrderTableValidator;
        this.tableGroupingService = tableGroupingService;
    }

    public TableGroup create(final CreateTableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
                                                .map(orderTableRequest -> orderTableRequest.getId())
                                                .collect(Collectors.toList());

        tableGroupOrderTableValidator.validateOrderTablesByIds(orderTableIds);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        tableGroupRepository.save(tableGroup);
        tableGroupingService.group(orderTableIds, tableGroup.getId());
        return tableGroup;
    }

    public void ungroup(final Long tableGroupId) {
        tableGroupingService.ungroup(tableGroupId);
    }
}
