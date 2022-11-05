package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.application.dto.request.OrderTableCreateCommand;
import kitchenpos.table.application.dto.request.OrderTableEmptyCommand;
import kitchenpos.table.application.dto.request.OrderTableGuestCommand;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderEmptyValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderEmptyValidator orderEmptyValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderEmptyValidator orderEmptyValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderEmptyValidator = orderEmptyValidator;
    }

    public OrderTableResponse create(final OrderTableCreateCommand orderTableCreateCommand) {
        OrderTable orderTable = orderTableCreateCommand.toEntity();
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .toList();
    }

    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableEmptyCommand orderTableEmptyCommand) {
        final OrderTable foundOrderTable = getOrderTable(orderTableId);
        foundOrderTable.changeEmpty(orderEmptyValidator, orderTableEmptyCommand.empty());

        return OrderTableResponse.from(foundOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableGuestCommand orderTableGuestCommand) {
        final OrderTable foundOrderTable = getOrderTable(orderTableId);
        foundOrderTable.changeNumberOfGuests(orderTableGuestCommand.numberOfGuests());
        return OrderTableResponse.from(foundOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }
}
