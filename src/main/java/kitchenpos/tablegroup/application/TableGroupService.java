package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.application.dto.TableGroupCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;


    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public Long create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.extractIds());
        tableGroupValidator.validate(orderTables, request.extractIds());
        final TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), orderTables);
        final TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        return saveTableGroup.getId();
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        final OrderTables orderTables = tableGroup.getOrderTables();
        tableGroupValidator.validateOrderTableStatus(orderTables);
        orderTables.reset();
    }
}
