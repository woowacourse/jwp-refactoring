package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.TableGroupNotFoundException;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.repository.TableGroupRepository;
import kitchenpos.domain.tablegroup.service.GroupingTableService;
import kitchenpos.ui.dto.request.CreateOrderGroupOrderTableRequest;
import kitchenpos.ui.dto.request.CreateTableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final GroupingTableService groupingTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final GroupingTableService groupingTableService,
            final TableGroupRepository tableGroupRepository
    ) {
        this.groupingTableService = groupingTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();
        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);

        groupingTableService.group(convertOrderTableIds(request), persistTableGroup);

        return persistTableGroup;
    }

    private List<Long> convertOrderTableIds(final CreateTableGroupRequest request) {
        return request.getOrderTables()
                      .stream()
                      .map(CreateOrderGroupOrderTableRequest::getId)
                      .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                          .orElseThrow(TableGroupNotFoundException::new);

        groupingTableService.ungroup(tableGroup);
    }
}
