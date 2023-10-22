package kitchenpos.application.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableCreationRequest;
import kitchenpos.application.dto.OrderTableEmptyStatusChangeRequest;
import kitchenpos.application.dto.OrderTableGuestAmountChangeRequest;
import kitchenpos.application.dto.result.OrderTableResult;
import kitchenpos.dao.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupingService tableGroupingService;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final TableGroupingService tableGroupingService
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupingService = tableGroupingService;
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
        tableGroupingService.isAbleToGroup(orderTableId);
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
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResult.from(orderTableRepository.save(orderTable));
    }
}
