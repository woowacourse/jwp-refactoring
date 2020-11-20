package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(
                new OrderTable(
                        null,
                        null,
                        orderTable.getNumberOfGuests(),
                        orderTable.isEmpty()
                )
        );
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validTableGroupIdIsNull(savedOrderTable.getTableGroupId());
        validOrderStatusIsNotCookingOrMeal(orderTableId);

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    private void validOrderStatusIsNotCookingOrMeal(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validTableGroupIdIsNull(Long tableGroupId) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        validOrderTableHasMinusNumberOfGuests(orderTable);

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validSavedOrderTableIsEmpty(savedOrderTable);

        savedOrderTable.setNumberOfGuests(orderTable.getNumberOfGuests());

        return orderTableRepository.save(savedOrderTable);
    }

    private void validSavedOrderTableIsEmpty(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validOrderTableHasMinusNumberOfGuests(OrderTable orderTable) {
        if (orderTable.hasMinusNumberOfGuests()) {
            throw new IllegalArgumentException();
        }
    }
}
