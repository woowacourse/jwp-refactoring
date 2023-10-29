package application;

import domain.OrderTable;
import domain.OrderTableRepository;
import dto.request.OrderTableCreateRequest;
import dto.request.OrderTableUpdateEmptyRequest;
import dto.request.OrderTableUpdateNumberOfGuestsRequest;
import dto.response.OrderTableResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(
            OrderTableRepository orderTableRepository
    ) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> readAll() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse updateIsEmpty(Long orderTableId, OrderTableUpdateEmptyRequest request) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);

        orderTable.changeEmpty(request.isEmpty());
        orderTableRepository.save(orderTable.publish());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse updateNumberOfGuests(Long orderTableId, OrderTableUpdateNumberOfGuestsRequest request) {
        int numberOfGuests = request.getNumberOfGuests();
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }
}
