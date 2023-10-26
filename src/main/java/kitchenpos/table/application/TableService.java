package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.TableValidator;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableService(
        final TableValidator tableValidator,
        final OrderTableRepository orderTableRepository,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderTableDto create(final OrderTableDto orderTableDto) {
        final OrderTable orderTable = new OrderTable(
            orderTableDto.getNumberOfGuests(),
            orderTableDto.getEmpty()
        );

        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableDto.from(savedOrderTable);
    }

    public List<OrderTableDto> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);
        tableValidator.validateChangeEmpty(savedOrderTable.getId());
        savedOrderTable.changeEmpty(orderTableDto.getEmpty());
        return OrderTableDto.from(savedOrderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableDto orderTableDto
    ) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTableDto.getNumberOfGuests());

        return OrderTableDto.from(savedOrderTable);
    }
}
