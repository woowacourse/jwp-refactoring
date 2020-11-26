package kitchenpos.ordertable.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.model.Order;
import kitchenpos.ordertable.model.OrderTable;
import kitchenpos.order.model.OrderVerifier;
import kitchenpos.ordertable.application.dto.OrderTableResponseDto;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;

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

    @Transactional(readOnly = true)
    public List<OrderTableResponseDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponseDto.listOf(orderTables);
    }

    @Transactional
    public OrderTableResponseDto changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = findBy(orderTableId);

        final List<Order> savedOrders = orderRepository.findAllByOrderTableId(orderTableId);
        OrderVerifier.validateNotCompleteOrderStatus(savedOrders);

        savedOrderTable.changeEmpty(isEmpty);
        OrderTable saved = orderTableRepository.save(savedOrderTable);

        return OrderTableResponseDto.from(saved);
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = findBy(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        final OrderTable saved = orderTableRepository.save(savedOrderTable);
        return OrderTableResponseDto.from(saved);
    }

    private OrderTable findBy(Long orderTableId) {
        if (Objects.nonNull(orderTableId)) {
            return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
        }
        throw new IllegalArgumentException("존재하지 않는 테이블입니다.");
    }
}
