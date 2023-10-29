package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateTableGroupDto;
import kitchenpos.application.exception.TableGroupNotFoundException;
import kitchenpos.domain.tablegroup.GroupingTableService;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.ui.dto.request.CreateOrderGroupOrderTableRequest;
import kitchenpos.ui.dto.request.CreateTableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public CreateTableGroupDto create(final CreateTableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();
        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);

        groupingTableService.group(convertOrderTableIds(request), persistTableGroup);

        return new CreateTableGroupDto(persistTableGroup);
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
