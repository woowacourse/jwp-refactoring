package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderTable;
import kitchenpos.infra.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(null, new NumberOfGuests(numberOfGuests), empty);
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, boolean empty) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeEmpty(empty);

        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(new NumberOfGuests(numberOfGuests));
        return orderTable;
    }
}
