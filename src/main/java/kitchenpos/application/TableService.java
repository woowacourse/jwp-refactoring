package kitchenpos.application;

import java.util.List;
import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.domain.ordertable.service.ChangeOrderTableStateService;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final ChangeOrderTableStateService changeOrderTableStateService;
    private final OrderTableRepository orderTableRepository;

    public TableService(
            final ChangeOrderTableStateService changeOrderTableStateService,
            final OrderTableRepository orderTableRepository
    ) {
        this.changeOrderTableStateService = changeOrderTableStateService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest request) {
        final OrderTable persistOrderTable = orderTableRepository.findById(orderTableId)
                                                                 .orElseThrow(OrderTableNotFoundException::new);

        changeOrderTableStateService.changeEmpty(persistOrderTable, request.isEmpty());

        return persistOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(
            final Long orderTableId,
            final UpdateOrderTableNumberOfGuestsRequest request
    ) {
        final OrderTable persistOrderTable = orderTableRepository.findById(orderTableId)
                                                                 .orElseThrow(OrderTableNotFoundException::new);

        persistOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return persistOrderTable;
    }
}
