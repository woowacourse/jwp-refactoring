package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.vo.NumberOfGuests;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeGuestNumberRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest numberOfGuestsAndEmptyStatus) {
        final OrderTable orderTable = new OrderTable(
                new NumberOfGuests(numberOfGuestsAndEmptyStatus.getNumberOfGuests()),
                numberOfGuestsAndEmptyStatus.isEmpty()
        );
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest emptyInput) {
        final OrderTable orderTable = orderTableRepository.findMandatoryById(orderTableId);
        validateTableGroupNotNull(orderTable);
        if (!orderTable.isEmpty()) {
            validateOrderStatusCompletion(orderTable);
        }
        orderTable.changeEmpty(emptyInput.getEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private void validateTableGroupNotNull(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatusCompletion(final OrderTable orderTable) {
        if (!orderRepository.existsByOrderTableIdAndCompletion(orderTable.getId())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeGuestNumberRequest numberOfGuestsInput
    ) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(numberOfGuestsInput.getNumberOfGuests());
        final OrderTable orderTable = orderTableRepository.findMandatoryById(orderTableId);
        validateOrderTableOccupied(orderTable);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private void validateOrderTableOccupied(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
