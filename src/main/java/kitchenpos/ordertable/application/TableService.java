package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.application.dto.TableChangeEmptyDto;
import kitchenpos.ordertable.application.dto.TableChangeNumberOfGuestsDto;
import kitchenpos.ordertable.application.dto.TableCreateDto;
import kitchenpos.ordertable.application.dto.TableDto;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableOrderStatusValidator tableOrderStatusValidator;

    public TableService(final OrderTableRepository orderTableRepository, final TableOrderStatusValidator tableOrderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableOrderStatusValidator = tableOrderStatusValidator;
    }

    @Transactional
    public TableDto create(final TableCreateDto request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        orderTableRepository.save(orderTable);

        return TableDto.toDto(orderTable);
    }

    public List<TableDto> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(TableDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(final Long orderTableId, final TableChangeEmptyDto request) {
        tableOrderStatusValidator.validateOrderIsCompleted(orderTableId);

        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.changeEmpty(request.isEmpty());

        return TableDto.toDto(orderTable);
    }

    @Transactional
    public TableDto changeNumberOfGuests(final Long orderTableId, final TableChangeNumberOfGuestsDto request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return TableDto.toDto(orderTable);
    }
}
