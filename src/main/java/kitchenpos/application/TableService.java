package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.ChangeOrderTableGuestRequest;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.exception.ExceptionInformation.*;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableRequest createOrderTableRequest) {
        final OrderTable orderTable = OrderTable.create(createOrderTableRequest.getNumberOfGuests());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyRequest changeOrderTableEmptyRequest) {
        final OrderTable orderTable = findUngroupedOrderTable(orderTableId);
        validateTableOrderStatusIsComplete(orderTableId);
        orderTable.updateOrderStatus(changeOrderTableEmptyRequest.isEmpty());
        return orderTable;
    }

    private OrderTable findUngroupedOrderTable(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new KitchenposException(ORDER_TABLE_NOT_FOUND));

        if (savedOrderTable.isGrouped()) {
            throw new KitchenposException(ORDER_TABLE_IS_GROUPING);
        }
        return savedOrderTable;
    }

    private void validateTableOrderStatusIsComplete(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getNotCompleteStatus())) {
            throw new KitchenposException(ORDER_TABLE_STATUS_IS_NOT_COMPLETE);
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeOrderTableGuestRequest changeOrderTableGuestRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new KitchenposException(ORDER_TABLE_NOT_FOUND));

        orderTable.updateGuest(changeOrderTableGuestRequest.getNumberOfGuests());
        return orderTable;
    }
}
