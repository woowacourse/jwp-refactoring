package kitchenpos.ordertable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final NoOngoingOrderValidator noOngoingOrderValidator;

    public TableService(OrderTableRepository orderTableRepository, NoOngoingOrderValidator noOngoingOrderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.noOngoingOrderValidator = noOngoingOrderValidator;
    }

    @Transactional
    public OrderTable create() {
        return orderTableRepository.save(new OrderTable());
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean empty) {
        OrderTable orderTable = orderTableRepository.getBy(orderTableId);
        noOngoingOrderValidator.validate(orderTable);

        if (empty) {
            orderTable.empty();
        }
        if (!empty) {
            orderTable.fill();
        }
        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.getBy(orderTableId);
        noOngoingOrderValidator.validate(orderTable);

        orderTable.fill(numberOfGuests);
        return orderTable;
    }
}
