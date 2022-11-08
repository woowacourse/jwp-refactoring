package kitchenpos.application;

import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTables;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.ordertable.TableValidator;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.domain.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableValidator tableValidator, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = createOrderTables(tableGroupRequest);
        orderTables.validatePossibleBindTableGroup();

        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllById(mapToIds(orderTables)));
        orderTables.validateMakeTableGroup(orderTables.getOrderTablesSize());

        final TableGroup tableGroup = new TableGroup(tableGroupRequest.getCreatedDate(), orderTables.getOrderTables());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTableRepository.saveAll(getOrderTables(savedOrderTables).getOrderTables());

        return savedTableGroup;
    }

    private OrderTables getOrderTables(OrderTables savedOrderTables) {
        return new OrderTables(savedOrderTables.getOrderTables()
                .stream()
                .map(orderTable -> new OrderTable(orderTable.getId(), orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(), false))
                .collect(Collectors.toList()));
    }

    private OrderTables createOrderTables(TableGroupRequest tableGroupRequest) {
        return new OrderTables(tableGroupRequest.getOrderTables()
                .stream()
                .map(it -> orderTableRepository.findById(it)
                        .orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList()));
    }

    private List<Long> mapToIds(OrderTables orderTables) {
        return orderTables.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        final List<Long> orderTableIds = mapToIds(orderTables);

        tableValidator.validateUnGroupCondition(orderTableIds);

        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            orderTableRepository.save(new OrderTable(orderTable.getId(),
                    null, orderTable.getNumberOfGuests(), orderTable.isEmpty()));
        }
    }
}
