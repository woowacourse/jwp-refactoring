package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        return savedOrderTable;
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
