package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.GroupOrderTableRequest;
import kitchenpos.application.dto.TableGroupingRequest;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    public TableGroup create(final TableGroupingRequest request) {
        final List<OrderTable> orderTables = request.getOrderTables().stream()
                .map(GroupOrderTableRequest::getId)
                .map(orderTableId -> orderTableRepository.findById(orderTableId)
                        .orElseThrow(() -> new IllegalArgumentException("Order table does not exist.")))
                .collect(Collectors.toList());
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroup.groupOrderTables(orderTables);
        return tableGroupRepository.save(tableGroup);
    }


    @Transactional
    public void ungroup(final Long ungroupTableId) {
        final TableGroup tableGroup = tableGroupRepository.findById(ungroupTableId)
                .orElseThrow(() -> new IllegalArgumentException("Table group does not exist."));
        tableGroup.ungroupOrderTables();
    }
}
