package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.GuestStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repositroy.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final GuestStatus guestStatus = new GuestStatus(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(new OrderTable(guestStatus)));
    }

    @Transactional
    public OrderTableResponse changeEmpty(
            final Long orderTableId,
            final OrderTableChangeEmptyRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeNumberOfGuestsRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuest(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAllByFetch()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
