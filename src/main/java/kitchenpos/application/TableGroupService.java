package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupCommand;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator,
                             OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCommand tableGroupCommand) {
        List<Long> orderTableId = tableGroupCommand.getOrderTableId();
        OrderTables orderTables = OrderTables.group(orderTableRepository.findAllByIdIn(orderTableId));
        tableGroupValidator.validate(orderTables, orderTableId);
        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        tableGroupValidator.validate(orderTables.getIds());
        orderTables.ungroup();
    }
}
