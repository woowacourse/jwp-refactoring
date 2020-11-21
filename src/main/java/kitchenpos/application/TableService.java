package kitchenpos.application;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableFindAllResponses;
import kitchenpos.dto.table.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.table.OrderTableUpdateEmptyResponse;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableUpdateNumberOfGuestsResponse;
import kitchenpos.dto.tableGroup.OrderTableCreateRequest;
import kitchenpos.dto.tableGroup.OrderTableCreateResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableCreateResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(
            new OrderTable(
                null,
                null,
                orderTableCreateRequest.getNumberOfGuests(),
                orderTableCreateRequest.getEmpty()
            )
        );
        return new OrderTableCreateResponse(savedOrderTable);
    }

    public OrderTableFindAllResponses findAll() {
        return OrderTableFindAllResponses.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableUpdateEmptyResponse changeEmpty(final Long orderTableId,
        final OrderTableUpdateEmptyRequest orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        validTableGroupIdIsNull(savedOrderTable.getTableGroupId());
        validOrderStatusIsNotCookingOrMeal(orderTableId);

        savedOrderTable.setEmpty(orderTable.getEmpty());

        return new OrderTableUpdateEmptyResponse(orderTableRepository.save(savedOrderTable));
    }

    private void validOrderStatusIsNotCookingOrMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private void validTableGroupIdIsNull(Long tableGroupId) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableUpdateNumberOfGuestsResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest) {
        validNumberOfGuestsUpdateIsMinus(orderTableUpdateNumberOfGuestsRequest);

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        validSavedOrderTableIsEmpty(savedOrderTable);

        savedOrderTable.setNumberOfGuests(orderTableUpdateNumberOfGuestsRequest.getNumberOfGuests());

        return new OrderTableUpdateNumberOfGuestsResponse(orderTableRepository.save(savedOrderTable));
    }

    private void validSavedOrderTableIsEmpty(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validNumberOfGuestsUpdateIsMinus(
        OrderTableUpdateNumberOfGuestsRequest orderTableUpdateNumberOfGuestsRequest) {
        if (orderTableUpdateNumberOfGuestsRequest.getNumberOfGuests() < 0) {
            throw new IllegalArgumentException();
        }
    }
}
