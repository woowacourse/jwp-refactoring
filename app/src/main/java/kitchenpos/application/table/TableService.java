package kitchenpos.application.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.table.dto.ChangeOrderTableEmptyCommand;
import kitchenpos.application.table.dto.ChangeOrderTableEmptyResponse;
import kitchenpos.application.table.dto.ChangeOrderTableNumberOfGuestsCommand;
import kitchenpos.application.table.dto.ChangeOrderTableNumberOfGuestsResponse;
import kitchenpos.application.table.dto.CreateOrderTableCommand;
import kitchenpos.application.table.dto.CreateOrderTableResponse;
import kitchenpos.application.table.dto.SearchOrderTableResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.event.TableEmptyChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final ApplicationEventPublisher publisher;
    private final OrderTableRepository orderTableRepository;

    public TableService(ApplicationEventPublisher publisher, OrderTableRepository orderTableRepository) {
        this.publisher = publisher;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public CreateOrderTableResponse create(CreateOrderTableCommand command) {
        OrderTable orderTable = new OrderTable(command.numberOfGuests(), command.empty());
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
        publisher.publishEvent(new TableEmptyChangedEvent(orderTable.id()));
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
