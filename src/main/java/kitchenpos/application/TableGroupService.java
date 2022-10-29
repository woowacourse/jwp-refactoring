package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupCommand;
import kitchenpos.domain.OrderTable;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dao.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupCommand tableGroupCommand) {
        List<Long> orderTableIds = tableGroupCommand.getOrderTableId();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validate(orderTableIds, orderTables);
        return tableGroupRepository.save(TableGroup.group(orderTables));
    }

    private void validate(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("주문 테이블의 수가 다릅니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        tableGroupValidator.validate(orderTables.getIds());
        orderTables.ungroup();
    }
}
