package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.Tables;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
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
        TableGroup save = tableGroupRepository.save(tableGroup);
    }
}
