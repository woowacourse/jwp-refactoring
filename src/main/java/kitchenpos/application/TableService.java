package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeEmptyOrderTableDto;
import kitchenpos.application.dto.ChangeNumberOfGuestsOrderTableDto;
import kitchenpos.application.dto.CreateOrderTableDto;
import kitchenpos.application.dto.ReadOrderTableDto;
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
@Transactional(readOnly = true)
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
    public CreateOrderTableDto create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable persistOrderTable = orderTableRepository.save(orderTable);

        return new CreateOrderTableDto(persistOrderTable);
    }

    public List<ReadOrderTableDto> list() {
        return orderTableRepository.findAll()
                                   .stream()
                                   .map(ReadOrderTableDto::new)
                                   .collect(Collectors.toList());
    }

    @Transactional
    public ChangeEmptyOrderTableDto changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest request) {
        final OrderTable persistOrderTable = orderTableRepository.findById(orderTableId)
                                                                 .orElseThrow(OrderTableNotFoundException::new);

        changeOrderTableStateService.changeEmpty(persistOrderTable, request.isEmpty());

        return new ChangeEmptyOrderTableDto(persistOrderTable);
    }

    @Transactional
    public ChangeNumberOfGuestsOrderTableDto changeNumberOfGuests(
            final Long orderTableId,
            final UpdateOrderTableNumberOfGuestsRequest request
    ) {
        final OrderTable persistOrderTable = orderTableRepository.findById(orderTableId)
                                                                 .orElseThrow(OrderTableNotFoundException::new);

        persistOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return new ChangeNumberOfGuestsOrderTableDto(persistOrderTable);
    }
}
