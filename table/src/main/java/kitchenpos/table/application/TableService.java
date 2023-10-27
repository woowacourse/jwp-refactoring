package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.event.TableEmptyChangedEvent;
import kitchenpos.table.application.dto.ChangeOrderTableEmptyCommand;
import kitchenpos.table.application.dto.ChangeOrderTableEmptyResponse;
import kitchenpos.table.application.dto.ChangeOrderTableNumberOfGuestsCommand;
import kitchenpos.table.application.dto.ChangeOrderTableNumberOfGuestsResponse;
import kitchenpos.table.application.dto.CreateOrderTableCommand;
import kitchenpos.table.application.dto.CreateOrderTableResponse;
import kitchenpos.table.application.dto.SearchOrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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
