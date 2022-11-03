package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.NotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.ui.dto.request.TableCreateRequest;
import kitchenpos.table.ui.dto.response.OrderTableChangeEmptyResponse;
import kitchenpos.table.ui.dto.response.OrderTableChangeNumberOfGuestsResponse;
import kitchenpos.table.ui.dto.response.TableCreateResponse;
import kitchenpos.table.ui.dto.response.TableFindAllResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private static final String NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE = "존재하지 않는 주문테이블입니다.";

    private final OrderTableRepository orderTableRepository;
    private final OrderStatusValidator orderStatusValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderStatusValidator orderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusValidator = orderStatusValidator;
    }

    @Transactional
    public TableCreateResponse create(final TableCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.save(toOrderTable(request));
        return TableCreateResponse.from(orderTable);
    }

    private OrderTable toOrderTable(final TableCreateRequest request) {
        return OrderTable.builder()
                .numberOfGuests(request.getNumberOfGuests())
                .empty(request.isEmpty())
                .build();
    }

    public List<TableFindAllResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return TableFindAllResponse.from(orderTables);
    }

    @Transactional
    public OrderTableChangeEmptyResponse changeEmpty(final Long orderTableId,
                                                     final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER_TABLE_ERROR_MESSAGE));

        if (orderStatusValidator.existsByOrderTableIdAndOrderStatusNotCompletion(orderTableId)) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(request.getEmpty());
        return OrderTableChangeEmptyResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableChangeNumberOfGuestsResponse changeNumberOfGuests(final Long orderTableId,
                                                                       final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        final int numberOfGuests = request.getNumberOfGuests();
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableChangeNumberOfGuestsResponse.from(savedOrderTable);
    }
}
