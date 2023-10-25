package kitchenpos.order.application;

import kitchenpos.order.application.dto.CreateOrderTableDto;
import kitchenpos.order.application.dto.OrderTableDto;
import kitchenpos.order.application.dto.UpdateOrderTableEmptyDto;
import kitchenpos.order.application.dto.UpdateOrderTableGuestNumberDto;
import kitchenpos.order.domain.GuestNumber;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.exception.OrderTableException;
import kitchenpos.order.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto create(CreateOrderTableDto createOrderTableDto) {
        OrderTable orderTable = new OrderTable(
                new GuestNumber(createOrderTableDto.getNumberOfGuests()),
                createOrderTableDto.getEmpty());

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableDto.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(UpdateOrderTableEmptyDto updateOrderTableEmptyDto) {
        Long id = updateOrderTableEmptyDto.getId();
        Boolean empty = updateOrderTableEmptyDto.getEmpty();

        OrderTable ordertable = findOrderTable(id);
        ordertable.changeEmpty(empty);

        return OrderTableDto.from(ordertable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(UpdateOrderTableGuestNumberDto updateOrderTableGuestNumberDto) {
        Long id = updateOrderTableGuestNumberDto.getId();
        GuestNumber numberOfGuests = new GuestNumber(updateOrderTableGuestNumberDto.getNumberOfGuests());

        OrderTable orderTable = findOrderTable(id);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableDto.from(orderTable);
    }

    private OrderTable findOrderTable(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new OrderTableException("해당 주문테이블을 찾을 수 없습니다."));
    }
}
