package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.tablegroup.CreateTableGroupCommand;
import kitchenpos.application.dto.tablegroup.UngroupTableGroupCommand;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public TableGroup create(CreateTableGroupCommand command) {
        List<Long> orderTableIds = command.orderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.empty() || Objects.nonNull(savedOrderTable.tableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        savedOrderTables.forEach(it -> it.setEmpty(false));
        TableGroup tableGroup = new TableGroup(savedOrderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(UngroupTableGroupCommand command) {
        tableGroupRepository.getById(command.tableGroupId())
                .ungroup();
    }
}
