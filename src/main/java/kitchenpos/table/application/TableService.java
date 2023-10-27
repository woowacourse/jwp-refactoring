package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.service.TableChangeEmptyValidator;
import kitchenpos.table.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.request.OrderTableChangeGuestRequest;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableChangeEmptyValidator tableChangeEmptyValidator;

    public TableService(OrderTableRepository orderTableRepository,
                        TableChangeEmptyValidator tableChangeEmptyValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableChangeEmptyValidator = tableChangeEmptyValidator;
    }

    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(
            new OrderTable(request.getNumberOfGuests(), request.isEmpty()));

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request) {
        OrderTable orderTable = orderTableRepository.findByIdOrThrow(orderTableId);
        orderTable.changeEmpty(tableChangeEmptyValidator);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableChangeGuestRequest request) {
        OrderTable orderTable = orderTableRepository.findByIdOrThrow(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}
