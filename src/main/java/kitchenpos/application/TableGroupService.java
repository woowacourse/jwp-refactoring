package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.request.OrderTableDto;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;


    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTables().stream()
            .map(OrderTableDto::getId)
            .collect(toList());
        List<OrderTable> savedOrderTables = getOrderTables(orderTableIds);
        return saveTableGroup(savedOrderTables);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문그룹입니다."));
        validateUngroupable(tableGroup.getOrderTables());
        tableGroup.ungroup();
    }

    private List<OrderTable> getOrderTables(List<Long> orderTableIds) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        return savedOrderTables;
    }

    private TableGroup saveTableGroup(List<OrderTable> savedOrderTables) {
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.createEmpty());
        tableGroup.group(savedOrderTables);
        return tableGroup;
    }

    private void validateUngroupable(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(toList());
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        if (orders.stream().anyMatch(Order::isNotCompletion)) {
            throw new IllegalArgumentException("주문이 완료 상태여만 그룹을 삭제할 수 있습니다.");
        }
    }
}
