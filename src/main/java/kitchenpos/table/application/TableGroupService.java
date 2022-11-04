package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.Tables;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final Tables tables = new Tables(toOrderTables(tableGroupRequest));
        tables.validateNoGroupAndEmpty();

        TableGroup entity = new TableGroup(LocalDateTime.now(), tables);
        TableGroup tableGroup = tableGroupRepository.save(entity);
        tableGroup.fillTables();
        tableGroup.placeOrderTables(new Tables(orderTableRepository.saveAll(tableGroup.getOrderTableValues())));
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> toOrderTables(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(orderTableRequest -> orderTableRepository.findById(orderTableRequest.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId);
        Tables tables = tableGroup.getOrderTables();
        orderRepository.validateComplete(tables.getOrderTableIds());
        tables.ungroup();
        tableGroupRepository.save(tableGroup);
    }
}
