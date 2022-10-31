package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.GuestNumber;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableEmptyChangeRequest;
import kitchenpos.dto.request.OrderTableGuestNumberChangeRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public TableService(OrderRepository orderRepository, TableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable =
                new OrderTable(new GuestNumber(orderTableCreateRequest.getNumberOfGuests()),
                        orderTableCreateRequest.isEmpty(), null);
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
