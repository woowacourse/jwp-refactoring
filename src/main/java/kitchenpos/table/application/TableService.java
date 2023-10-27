package kitchenpos.table.application;

import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableEmptyUpdateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableNumberOfGuestsUpdateRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderTableValidator orderTableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable newOrderTable = OrderTable.withoutTableGroup(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(newOrderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyUpdateRequest request) {
        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(orderTableId);
        findOrderTable.changeOrderTableEmpty(orderTableValidator, request.isEmpty());

        return OrderTableResponse.from(findOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final TableNumberOfGuestsUpdateRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(orderTableId);
        findOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(findOrderTable);
    }
}
