package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.tablegroup.CreateTableGroupCommand;
import kitchenpos.application.dto.tablegroup.CreateTableGroupResponse;
import kitchenpos.application.dto.tablegroup.UngroupTableGroupCommand;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public CreateTableGroupResponse create(CreateTableGroupCommand command) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdInOrElseThrow(command.orderTableIds());
        TableGroup tableGroup = new TableGroup(orderTables);
        return CreateTableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(UngroupTableGroupCommand command) {
        tableGroupRepository.getById(command.tableGroupId())
                .ungroup();
    }
}
