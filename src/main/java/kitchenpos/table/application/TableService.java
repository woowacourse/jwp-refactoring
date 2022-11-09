package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableEmptyUpdateRequest;
import kitchenpos.table.application.dto.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableDomainService tableDomainService;

    public TableService(final OrderTableRepository orderTableRepository, final TableDomainService tableDomainService) {
        this.orderTableRepository = orderTableRepository;
        this.tableDomainService = tableDomainService;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.save(request.toOrderTable());
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.from(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyUpdateRequest request) {
        final OrderTable orderTable = tableDomainService.changeEmpty(orderTableId, request.getEmpty());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableNumberOfGuestsUpdateRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderTable orderTable = savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }
}
