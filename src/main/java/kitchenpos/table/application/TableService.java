package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.dto.OrderTableRequestDto;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.UpdateEmptyRequestDto;
import kitchenpos.table.application.dto.UpdateNumberOfGuestRequestDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequestDto orderTableRequestDto) {
        final OrderTable savedOrderTable = orderTableRepository.save(
                new OrderTable(orderTableRequestDto.getNumberOfGuests(), orderTableRequestDto.isEmpty()));
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final UpdateEmptyRequestDto updateEmptyRequestDto) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.validateNotGroupTable();
        validateCookingOrMeal(orderTableId);

        final OrderTable updateOrderTable = orderTableRepository.save(
                new OrderTable(
                        savedOrderTable.getTableGroupId(),
                        savedOrderTable.getTableGroupId(),
                        savedOrderTable.getNumberOfGuests(),
                        updateEmptyRequestDto.getEmpty()
                        ));
        return OrderTableResponse.of(updateOrderTable);
    }

    private void validateCookingOrMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final UpdateNumberOfGuestRequestDto updateNumberofGuestRequestDto) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.validateNotEmptyTable();
        final OrderTable savedOrderTable = orderTableRepository.save(
                new OrderTable(
                        orderTableId,
                        orderTable.getTableGroupId(),
                        updateNumberofGuestRequestDto.getNumberOfGuests(),
                        orderTable.isEmpty()));
        return OrderTableResponse.of(savedOrderTable);
    }
}
