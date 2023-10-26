package kitchenpos.ordertable.application;

import kitchenpos.ordertable.application.dto.CreateOrderTableDto;
import kitchenpos.ordertable.application.dto.OrderTableDto;
import kitchenpos.ordertable.application.dto.UpdateOrderTableEmptyDto;
import kitchenpos.ordertable.application.dto.UpdateOrderTableGuestNumberDto;
import kitchenpos.ordertable.domain.GuestNumber;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderValidator;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public TableService(
            OrderTableRepository orderTableRepository,
            OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
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
        ordertable.changeEmpty(empty, orderValidator);

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
