package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.UngroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final UngroupValidator ungroupValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final UngroupValidator ungroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.ungroupValidator = ungroupValidator;
    }

    @Transactional
    public TableGroupDto create(final CreateTableGroupCommand command) {
        final List<Long> orderTableIds = command.getOrderTableIds();
        final List<OrderTable> foundOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (invalidOrderTable(orderTableIds, foundOrderTables)) {
            throw new IllegalArgumentException("유효하지 않은 테이블이 있습니다.");
        }

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.changeOrderTables(foundOrderTables);

        return TableGroupDto.from(tableGroup);
    }

    private boolean invalidOrderTable(final List<Long> orderTableIds, final List<OrderTable> foundOrderTables) {
        return orderTableIds.size() != foundOrderTables.size();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroup(ungroupValidator);
    }

}
