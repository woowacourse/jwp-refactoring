package kitchenpos.ordertable.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.request.OrderTableCreateRequest;
import kitchenpos.ordertable.request.TableChangeEmptyRequest;
import kitchenpos.ordertable.request.TableChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuest(), request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, TableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTableById(orderTableId);
        validateOrderStatus(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                            .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, TableChangeNumberOfGuestsRequest request) {
        OrderTable savedOrderTable = getOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public OrderTables findAllByTableIds(List<Long> orderTableIds) {
        return new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    public void checkOrderStatusInCookingOrMeal(OrderTables orderTables) {
        List<Long> orderTableIds = orderTables.getIds();
        List<OrderStatus> invalidOrderStatusToUngroup = List.of(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, invalidOrderStatusToUngroup)) {
            throw new IllegalArgumentException();
        }
    }
}
