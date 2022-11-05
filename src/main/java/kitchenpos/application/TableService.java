package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.TableDto;
import kitchenpos.domain.service.FindOrderTableInOrderStatusService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final FindOrderTableInOrderStatusService findOrderTableInOrderStatusService;

    public TableService(final OrderTableRepository orderTableRepository,
                        final FindOrderTableInOrderStatusService findOrderTableInOrderStatusService) {
        this.orderTableRepository = orderTableRepository;
        this.findOrderTableInOrderStatusService = findOrderTableInOrderStatusService;
    }

    @Transactional
    public TableDto create(final Integer numberOfGuests, final Boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return TableDto.of(orderTableRepository.save(orderTable));
    }

    public List<TableDto> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(final Long orderTableId, final Boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateGroupedTable(savedOrderTable);
        savedOrderTable.validateEmptyAvailable(findOrderTableInOrderStatusService);

        savedOrderTable.changeEmpty(empty);

        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }

    private void validateGroupedTable(final OrderTable savedOrderTable) {
        if (savedOrderTable.isGrouped()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public TableDto changeNumberOfGuests(final Long orderTableId, final Integer numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.enterGuests(numberOfGuests);

        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }

    private void validateNumberOfGuests(final Integer numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }
}
