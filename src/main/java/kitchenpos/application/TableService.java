package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.table.OrderTableChangeRequestDto;
import kitchenpos.application.dto.request.table.OrderTableCreateRequestDto;
import kitchenpos.application.dto.response.table.OrderTableResponseDto;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponseDto create(OrderTableCreateRequestDto orderTableCreateRequestDto) {
        OrderTable orderTable =
            new OrderTable(orderTableCreateRequestDto.getNumberOfGuests(), orderTableCreateRequestDto.isEmpty());
        return OrderTableResponseDto.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponseDto> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponseDto changeEmpty(OrderTableChangeRequestDto orderTableChangeRequestDto) {
        OrderTable orderTable = orderTableRepository.findById(orderTableChangeRequestDto.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("OrderTable이 존재하지 않습니다."));
        orderTable.changeEmpty(orderTableChangeRequestDto.isEmpty());
        return OrderTableResponseDto.from(orderTable);
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(OrderTableChangeRequestDto orderTableChangeRequestDto) {
        OrderTable orderTable = orderTableRepository.findById(orderTableChangeRequestDto.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("OrderTable이 존재하지 않습니다."));
        orderTable.changeNumberOfGuests(orderTableChangeRequestDto.getNumberOfGuests());
        return OrderTableResponseDto.from(orderTable);
    }
}
