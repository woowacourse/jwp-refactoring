package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.TableDto;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableDto create(final Integer numberOfGuests, final Boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return TableDto.of(savedOrderTable);
    }

    public List<TableDto> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(final Long orderTableId, final Boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(empty);

        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public TableDto changeNumberOfGuests(final Long orderTableId, final Integer numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.enterGuests(numberOfGuests);

        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }
}
