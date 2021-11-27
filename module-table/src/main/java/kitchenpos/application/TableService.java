package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderTableEmptyRequestDto;
import kitchenpos.dto.request.OrderTableGuestRequestDto;
import kitchenpos.dto.request.OrderTableRequestDto;
import kitchenpos.dto.response.OrderTableResponseDto;
import kitchenpos.exception.InvalidOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableDomainService tableDomainService;

    public TableService(
        OrderTableRepository orderTableRepository,
        TableDomainService tableDomainService
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableDomainService = tableDomainService;
    }

    @Transactional
    public OrderTableResponseDto create(final OrderTableRequestDto orderTableRequestDto) {
        OrderTable created = orderTableRepository.save(new OrderTable(
            orderTableRequestDto.getNumberOfGuests(),
            orderTableRequestDto.getEmpty()
        ));

        return toOrderTableResponseDto(created);
    }

    public List<OrderTableResponseDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(this::toOrderTableResponseDto)
            .collect(toList());
    }

    private OrderTableResponseDto toOrderTableResponseDto(OrderTable orderTable) {
        return new OrderTableResponseDto(
            orderTable.getId(),
            orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    @Transactional
    public OrderTableResponseDto changeEmpty(
        final Long orderTableId,
        final OrderTableEmptyRequestDto orderTableEmptyRequestDto
    ) {
        return tableDomainService.changeEmpty(orderTableId, orderTableEmptyRequestDto);
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(final Long orderTableId,
        final OrderTableGuestRequestDto orderTableGuestRequestDto) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(orderTableGuestRequestDto.getNumberOfGuests());

        return new OrderTableResponseDto(
            orderTable.getId(),
            orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(InvalidOrderTableException::new);
    }
}
