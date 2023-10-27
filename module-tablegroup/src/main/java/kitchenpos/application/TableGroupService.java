package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupValidator;
import kitchenpos.infra.OrderTableRepository;
import kitchenpos.infra.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupValidator validator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            TableGroupValidator validator,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.validator = validator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.getAllByIdIn(orderTableIds);
        validator.validate(orderTableIds.size(), orderTables);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        for (OrderTable orderTable : orderTables) {
            orderTable.group(tableGroup.id());
        }
        return tableGroup;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);

        List<OrderTable> orderTables = orderTableRepository.getAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
        tableGroupRepository.delete(tableGroup);
    }
}
