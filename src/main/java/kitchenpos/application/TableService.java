package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.TableCreateRequest;
import kitchenpos.dto.table.TableUpdateEmptyRequest;
import kitchenpos.dto.table.TableUpdateNumberOfGuestsRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable create(final TableCreateRequest request) {
        OrderTable table = OrderTable.of(null, request.getNumberOfGuests(), request.isEmpty());
        return orderTableRepository.save(table);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> readAll() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final TableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateTableGroup(savedOrderTable);
        validateOrderStatus(savedOrderTable);
        savedOrderTable.updateEmpty(request.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    private void validateTableGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("이미 속해있는 table group이 있습니다.");
        }
    }

    private void validateOrderStatus(final OrderTable savedOrderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                savedOrderTable, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableUpdateNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }
}
