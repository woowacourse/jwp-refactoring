package kitchenpos.table.application;

import kitchenpos.global.exception.KitchenposException;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.table.ui.dto.ChangeOrderTableGuestRequest;
import kitchenpos.table.ui.dto.CreateOrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.global.exception.ExceptionInformation.ORDER_TABLE_IS_GROUPING;
import static kitchenpos.global.exception.ExceptionInformation.ORDER_TABLE_NOT_FOUND;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    private final OrderTableStatusValidator orderTableStatusValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderTableStatusValidator orderTableStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableStatusValidator = orderTableStatusValidator;
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
        orderTableStatusValidator.validateIsComplete(orderTable.getId());
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

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeOrderTableGuestRequest changeOrderTableGuestRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new KitchenposException(ORDER_TABLE_NOT_FOUND));

        orderTable.updateGuest(changeOrderTableGuestRequest.getNumberOfGuests());
        return orderTable;
    }
}
