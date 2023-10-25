package kitchenpos.application;

import static java.util.stream.Collectors.toList;
import static kitchenpos.exception.ExceptionType.ORDER_TABLE_CANNOT_CHANGE_STATUS;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTable.Builder;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderService orderService;
    private final OrderTableDao orderTableDao;

    public TableService(
        final OrderService orderService,
        final OrderTableDao orderTableDao
    ) {
        this.orderService = orderService;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableDto create(final OrderTableDto orderTableDto) {
        OrderTable orderTable = new Builder()
            .setNumberOfGuests(orderTableDto.getNumberOfGuests())
            .setEmpty(orderTableDto.isEmpty())
            .build();

        return OrderTableDto.from(orderTableDao.save(orderTable));
    }

    public List<OrderTableDto> list() {
        return orderTableDao.findAll()
                            .stream()
                            .map(OrderTableDto::from)
                            .collect(toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable foundOrderTable = findById(orderTableId);

        List<Order> cookingOrders = orderService.findByOrderTableIdAndOrderStatus(orderTableId, OrderStatus.COOKING);
        List<Order> mealOrders = orderService.findByOrderTableIdAndOrderStatus(orderTableId, OrderStatus.MEAL);
        if (!cookingOrders.isEmpty() || !mealOrders.isEmpty()) {
            throw new CustomException(ORDER_TABLE_CANNOT_CHANGE_STATUS);
        }

        foundOrderTable.changeEmpty(orderTableDto.isEmpty());

        return OrderTableDto.from(foundOrderTable);
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                            .orElseThrow(IllegalArgumentException::new);
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
}
