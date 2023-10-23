package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableCreationRequest;
import kitchenpos.table.application.dto.OrderTableEmptyStatusChangeRequest;
import kitchenpos.table.application.dto.OrderTableGuestAmountChangeRequest;
import kitchenpos.table.application.dto.OrderTableResult;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableChangeNumberOfGuestValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableChangeNumberOfGuestValidator tableChangeNumberOfGuestValidator;
    private final OrderTableValidator ordersEmptyValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final TableChangeNumberOfGuestValidator tableChangeNumberOfGuestValidator,
            final OrderTableValidator ordersEmptyValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableChangeNumberOfGuestValidator = tableChangeNumberOfGuestValidator;
        this.ordersEmptyValidator = ordersEmptyValidator;
    }

    @Transactional
    public OrderTableResult create(final OrderTableCreationRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResult> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResult::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResult changeEmpty(final Long orderTableId, final OrderTableEmptyStatusChangeRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        orderTable.changeEmpty(request.getEmpty(), ordersEmptyValidator);
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResult changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableGuestAmountChangeRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        orderTable.changeNumberOfGuests(request.getNumberOfGuests(), tableChangeNumberOfGuestValidator);
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }
}
