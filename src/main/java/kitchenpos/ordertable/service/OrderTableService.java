package kitchenpos.ordertable.service;

import static java.util.stream.Collectors.toList;
import static kitchenpos.exception.ExceptionType.ORDER_TABLE_CANNOT_CHANGE_STATUS;
import static kitchenpos.exception.ExceptionType.ORDER_TABLE_NOT_FOUND;

import java.util.List;
import kitchenpos.exception.CustomException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.service.OrderService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTable.Builder;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(
        final OrderService orderService,
        final OrderTableRepository orderTableRepository,
        final OrderRepository orderRepository
    ) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableDto create(final OrderTableDto orderTableDto) {
        OrderTable orderTable = new Builder()
            .setNumberOfGuests(orderTableDto.getNumberOfGuests())
            .setEmpty(orderTableDto.isEmpty())
            .build();

        return OrderTableDto.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableDto> list() {
        return orderTableRepository.findAll()
                                   .stream()
                                   .map(OrderTableDto::from)
                                   .collect(toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable foundOrderTable = findById(orderTableId);

        List<Order> cookingOrders = orderRepository.findByOrderTableIdAndOrderStatus(orderTableId,
            OrderStatus.COOKING);
        List<Order> mealOrders = orderRepository.findByOrderTableIdAndOrderStatus(orderTableId,
            OrderStatus.MEAL);
        if (!cookingOrders.isEmpty() || !mealOrders.isEmpty()) {
            throw new CustomException(ORDER_TABLE_CANNOT_CHANGE_STATUS);
        }

        foundOrderTable.changeEmpty(orderTableDto.isEmpty());

        return OrderTableDto.from(foundOrderTable);
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new CustomException(ORDER_TABLE_NOT_FOUND));
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableDto orderTableDto
    ) {
        OrderTable orderTable = findById(orderTableId);

        orderTable.changeNumberOfGuests(orderTableDto.getNumberOfGuests());

        return OrderTableDto.from(orderTable);
    }

    public OrderTable getById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new CustomException(ORDER_TABLE_NOT_FOUND));
    }
}
