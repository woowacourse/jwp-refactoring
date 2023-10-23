package kitchenpos.application;

import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.NotExistOrderTable;
import kitchenpos.domain.exception.NotExistTableGroupException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    
    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }
    
    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = getOrderTables(request.getOrderTables());
        TableGroup tableGroup = new TableGroup(savedOrderTables);
        updateOrderTables(tableGroup, savedOrderTables);
        return tableGroup;
    }
    
    private List<OrderTable> getOrderTables(final List<Long> OrderTableIds) {
        return OrderTableIds.stream()
                            .map(orderTableId -> orderTableRepository.findById(orderTableId)
                                                                     .orElseThrow(() -> new NotExistOrderTable("존재하지 않는 테이블입니다")))
                            .collect(Collectors.toList());
    }
    
    private void updateOrderTables(final TableGroup tableGroup, final List<OrderTable> savedOrderTables) {
        final List<OrderTable> orderTables = savedOrderTables.stream()
                                                             .map(orderTable -> new OrderTable(orderTable.getId(),
                                                                     tableGroup,
                                                                     orderTable.getNumberOfGuests(),
                                                                     orderTable.isEmpty()))
                                                             .collect(Collectors.toList());
        orderTableRepository.saveAll(orderTables);
    }
    
    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(() -> new NotExistTableGroupException("존재하지 않는 단체 테이블입니다"));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<List<Order>> ordersInTables = orderTables.stream()
                                                            .map(orderTable -> orderRepository.findAllByOrderTableId(orderTable.getId()))
                                                            .collect(Collectors.toList());
        tableGroup.ungroup(orderTables, ordersInTables);
        tableGroupRepository.save(tableGroup);
    }
}
