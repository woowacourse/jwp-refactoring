package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableCreationRequest;
import kitchenpos.table.application.dto.OrderTableEmptyStatusChangeRequest;
import kitchenpos.table.application.dto.OrderTableGuestAmountChangeRequest;
import kitchenpos.order.application.dto.OrderTableResult;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidationService tableValidationService;
    private final TableValidator tableValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final TableValidationService tableValidationService,
            final TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidationService = tableValidationService;
        this.tableValidator = tableValidator;
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
        tableValidationService.validateChangeEmptyAvailable(orderTableId);
        orderTable.changeEmpty(request.getEmpty());
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }

    @Transactional
    public OrderTableResult changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableGuestAmountChangeRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        orderTable.changeNumberOfGuests(request.getNumberOfGuests(), tableValidator);
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }
}
