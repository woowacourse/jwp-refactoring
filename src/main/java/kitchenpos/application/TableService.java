package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.application.dto.request.UpdateOrderTableGuestsRequest;
import kitchenpos.application.dto.response.CreateOrderTableResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.mapper.OrderTableMapper;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public CreateOrderTableResponse create(final CreateOrderTableRequest request) {
        OrderTable entity = OrderTableMapper.toOrderTable(request);
        OrderTable saved = orderTableRepository.save(entity);
        return CreateOrderTableResponse.from(saved);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest request) {
        final OrderTable entity = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderStatus(orderTableId);
        OrderTable updated = entity.updateEmpty(request.isEmpty());
        OrderTable save = orderTableRepository.save(updated);
        return OrderTableResponse.from(save);
    }

    private void validateOrderStatus(Long orderTableId) {
        Order order = orderRepository.findByOrderTableId(orderTableId);
        if (order.getOrderStatus() == COOKING || order.getOrderStatus() == MEAL) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final UpdateOrderTableGuestsRequest request) {
        final OrderTable entity = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderTable updated = entity.updateNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(updated));
    }
}
