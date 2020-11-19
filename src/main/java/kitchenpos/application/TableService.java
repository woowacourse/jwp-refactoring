package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTable orderTable) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        validateCanChangeEmpty(orderTableId, savedOrderTable);
        savedOrderTable = savedOrderTable.changeEmpty(orderTable.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    private void validateCanChangeEmpty(Long orderTableId, OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("table group에 속해있는 테이블은 empty 수정이 불가합니다.");
        }
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사중일 땐 empty 수정이 불가합니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, OrderTable orderTable) {
        int numberOfGuests = orderTable.getNumberOfGuests();
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable = savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 order table id 입니다."));
    }
}
