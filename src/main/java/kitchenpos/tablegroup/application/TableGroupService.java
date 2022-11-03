package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.application.request.OrderTableGroupCreateRequest;
import kitchenpos.tablegroup.application.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDomainService;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupDomainService tableGroupDomainService;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final TableGroupDomainService tableGroupDomainService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupDomainService = tableGroupDomainService;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        final List<Long> orderTableIds = getOrderTableIds(request);
        tableGroupDomainService.addOrderTable(tableGroup, orderTableIds);
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupDomainService.ungroup(tableGroupId);
    }

    private List<Long> getOrderTableIds(final TableGroupCreateRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableGroupCreateRequest::getId)
                .collect(Collectors.toList());
    }
}
