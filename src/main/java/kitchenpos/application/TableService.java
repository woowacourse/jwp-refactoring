package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableResponseDto;
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
    public OrderTableResponseDto create() {
        OrderTable saved = orderTableRepository.save(new OrderTable());
        return OrderTableResponseDto.from(saved);
    }

    public List<OrderTableResponseDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponseDto.listOf(orderTables);
    }

    @Transactional
    public OrderTableResponseDto changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태이블입니다."));

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("테이블에 들어간 주문 중 '조리' 또는 '식사 중' 상태인 주문이 남아있습니다.");
        }

        savedOrderTable.changeEmpty(isEmpty);
        OrderTable saved = orderTableRepository.save(savedOrderTable);

        return OrderTableResponseDto.from(saved);
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        OrderTable saved = orderTableRepository.save(savedOrderTable);
        return OrderTableResponseDto.from(saved);
    }
}
