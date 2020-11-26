package kitchenpos.application.table;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.table.response.OrderTableResponse;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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
    public OrderTable changeEmpty(Long orderTableId, OrderTable expectedTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사중일 땐 empty 수정이 불가합니다.");
        }
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        OrderTable changedTable = savedOrderTable.changeEmpty(expectedTable.isEmpty());
        return orderTableRepository.save(changedTable);
    }


    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, OrderTable expectedTable) {
        int replacedNumberOfGuests = expectedTable.getNumberOfGuests();
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        return savedOrderTable.changeNumberOfGuests(replacedNumberOfGuests);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 order table id 입니다."));
    }
}
