package kitchenpos.application;

import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.mapper.OrderTableMapper;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable orderTable = OrderTableMapper.mapToOrderTable(orderTableCreateRequest);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableMapper.mapToResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (!Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
        if (isChangeEmptyUnable(orderTable)) {
            throw new IllegalArgumentException();
        }
        orderTable.changeEmpty(isEmpty);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableMapper.mapToResponse(savedOrderTable);
    }

    private boolean isChangeEmptyUnable(final OrderTable orderTable) {
        return orderRepository.findAllByOrderTable(orderTable)
                .stream()
                .anyMatch(this::containOrderStatusCookingOrMeal);
    }

    private boolean containOrderStatusCookingOrMeal(final Order order) {
        return order.isOrderStatusEquals(OrderStatus.COOKING)
                || order.isOrderStatusEquals(OrderStatus.MEAL);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        orderTable.changeNumberOfGuests(numberOfGuests);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableMapper.mapToResponse(savedOrderTable);
    }
}
