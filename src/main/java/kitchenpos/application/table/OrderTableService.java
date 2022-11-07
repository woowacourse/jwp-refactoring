package kitchenpos.application.table;


import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.request.OrderTableChangeGuestNumberRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderTableService(final OrderTableRepository orderTableRepository,
                             final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toTable()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long tableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(tableId);
        orderTable.changeEmpty(request.getEmpty(), orderTableValidator);
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long tableId, final OrderTableChangeGuestNumberRequest request) {
        final OrderTable orderTable = findOrderTableById(tableId);
        orderTable.changeGuestNumber(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTableById(final Long tableId) {
        return orderTableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException(CustomError.TABLE_NOT_FOUND_ERROR));
    }
}
