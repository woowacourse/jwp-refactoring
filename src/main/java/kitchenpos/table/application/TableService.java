package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto create(final CreateTableCommand command) {
        OrderTable table = orderTableRepository.save(command.toDomain());
        return OrderTableDto.from(table);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTableDto changeEmpty(final ChangeTableEmptyCommand request) {
        Long tableId = request.getTableId();
        final OrderTable orderTable = orderTableRepository.getById(tableId);

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                tableId, Arrays.asList(OrderStatus.NOT_STARTED, OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사인 경우 테이블의 비어있을 수 없습니다.");
        }

        orderTable.changeEmpty(request.getEmpty());
        return OrderTableDto.from(orderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final ChangeNumberOfGuestsCommand request) {
        final OrderTable foundTable = orderTableRepository.getById(request.getOrderTableId());

        foundTable.changeNumberOfGuests(request.getNumberOfGuests());
        OrderTable orderTable = orderTableRepository.save(foundTable);
        return OrderTableDto.from(orderTable);
    }

}