package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyCommand;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsCommand;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(ChangeOrderTableEmptyCommand command) {
        OrderTable savedOrderTable = orderTableRepository.getById(command.id());
        savedOrderTable.changeEmpty(command.empty());
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(ChangeOrderTableNumberOfGuestsCommand command) {
        OrderTable savedOrderTable = orderTableRepository.getById(command.id());
        savedOrderTable.changeNumberOfGuests(command.numberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }
}
