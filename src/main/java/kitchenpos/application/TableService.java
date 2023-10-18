package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyCommand;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyResponse;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsCommand;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsResponse;
import kitchenpos.application.dto.ordertable.CreateOrderTableCommand;
import kitchenpos.application.dto.ordertable.CreateOrderTableResponse;
import kitchenpos.application.dto.ordertable.SearchOrderTableResponse;
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
    public CreateOrderTableResponse create(CreateOrderTableCommand command) {
        OrderTable orderTable = new OrderTable(null, null, command.numberOfGuests(), command.empty());
        return CreateOrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<SearchOrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(SearchOrderTableResponse::from)
                .collect(Collectors.toList());
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
