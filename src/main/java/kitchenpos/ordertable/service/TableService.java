package kitchenpos.ordertable.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.GuestNumber;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyChangeRequest;
import kitchenpos.ordertable.dto.OrderTableGuestNumberChangeRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.dto.OrderTableValidateEvent;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import kitchenpos.ordertable.repository.TableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final TableRepository tableRepository;

    public TableService(ApplicationEventPublisher applicationEventPublisher,
                        TableRepository tableRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = new OrderTable(
                new GuestNumber(orderTableCreateRequest.getNumberOfGuests()), orderTableCreateRequest.isEmpty());
        OrderTable savedOrderTable = tableRepository.save(orderTable);
        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return tableRepository.findAll()
                .stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId,
                                          OrderTableEmptyChangeRequest orderTableEmptyChangeRequest) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        applicationEventPublisher.publishEvent(new OrderTableValidateEvent(orderTableId));
        savedOrderTable.setEmpty(orderTableEmptyChangeRequest.isEmpty());
        return new OrderTableResponse(tableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableGuestNumberChangeRequest
            orderTableGuestNumberChangeRequest) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeGuestNumber(new GuestNumber(orderTableGuestNumberChangeRequest.getNumberOfGuests()));
        return new OrderTableResponse(tableRepository.save(savedOrderTable));
    }
}
