package kitchenpos.application.tablegroup;

import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        TableGroup tableGroup = TableGroup.create();
        tableGroup.group(orderTableRepository.findAllByIdIn(request.getOrderTableIds()));
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup);
    }
}
