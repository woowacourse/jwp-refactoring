package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public List<OrderTable> addTableGroup(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (final OrderTable updateOrderTable : savedOrderTables) {
            updateOrderTable.addTableGroup(savedTableGroup);
            orderTables.add(updateOrderTable);
        }
        return orderTables;
    }

    @Transactional
    public void addTableGroup2(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        for (final OrderTable updateOrderTable : savedOrderTables) {
            updateOrderTable.addTableGroup(savedTableGroup);
        }
    }

    public List<OrderTable> findAllOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTableIds, orderTables);
        return orderTables;
    }

    private void validateOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("요청과 조회 값의 결과가 다릅니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTable.getId()));
            orders.checkNotCompleted();
            orderTable.ungroup();
        }
    }
}
