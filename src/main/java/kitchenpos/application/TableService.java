package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyCommand;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyResponse;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsCommand;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsResponse;
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
    public ChangeOrderTableEmptyResponse changeEmpty(ChangeOrderTableEmptyCommand command) {
        OrderTable orderTable = orderTableRepository.getById(command.id());
        orderTable.changeEmpty(command.empty());
        return ChangeOrderTableEmptyResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional
    public ChangeOrderTableNumberOfGuestsResponse changeNumberOfGuests(ChangeOrderTableNumberOfGuestsCommand command) {
        OrderTable orderTable = orderTableRepository.getById(command.id());
        orderTable.changeNumberOfGuests(command.numberOfGuests());
        return ChangeOrderTableNumberOfGuestsResponse.from(orderTableRepository.save(orderTable));
    }
}
