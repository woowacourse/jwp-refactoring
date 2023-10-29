package kitchenpos.table.service;

import kitchenpos.table.OrderTableValidator;
import kitchenpos.table.OrderTables;
import kitchenpos.table.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.service.dto.TableGroupCreateRequest;
import kitchenpos.table.service.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository, final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables();
        final OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByIdIn(orderTableIds));

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(now()));
        orderTables.changeTableGroup(savedTableGroup);
        return TableGroupResponse.toDto(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderTables.validate(orderTableValidator);

        orderTables.ungroupAll();
    }
}
