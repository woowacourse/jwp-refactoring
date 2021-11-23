package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.OrderTableDtoAssembler;
import kitchenpos.application.dto.request.OrderTableRequestDto;
import kitchenpos.application.dto.response.OrderTableResponseDto;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrdersRepository ordersRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(
        final OrdersRepository ordersRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponseDto create(OrderTableRequestDto requestDto) {
        OrderTable orderTable = orderTableRepository
            .save(new OrderTable(requestDto.getNumberOfGuests(), requestDto.getEmpty()));

        return OrderTableDtoAssembler.orderTableResponseDto(orderTable);
    }

    public List<OrderTableResponseDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
            .map(OrderTableDtoAssembler::orderTableResponseDto)
            .collect(toList());
    }

//    @Transactional
//    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
//        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
//            .orElseThrow(IllegalArgumentException::new);
//
//        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
//            throw new IllegalArgumentException();
//        }
//
//        if (ordersRepository.existsByOrderTableIdAndOrderStatusIn(
//            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }
//
//        savedOrderTable.changeEmpty(orderTable.isEmpty());
//
//        return orderTableRepository.save(savedOrderTable);
//    }
//
//    @Transactional
//    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
//        final int numberOfGuests = orderTable.getNumberOfGuests();
//
//        if (numberOfGuests < 0) {
//            throw new IllegalArgumentException();
//        }
//
//        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
//            .orElseThrow(IllegalArgumentException::new);
//
//        if (savedOrderTable.isEmpty()) {
//            throw new IllegalArgumentException();
//        }
//
//        savedOrderTable.changeNumberOfGuests(numberOfGuests);
//
//        return orderTableRepository.save(savedOrderTable);
//    }
}
