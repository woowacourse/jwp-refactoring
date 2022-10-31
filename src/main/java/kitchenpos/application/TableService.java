package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.GuestNumber;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableDto;
import kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public TableService(OrderRepository orderRepository, TableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public TableDto create(TableDto tableDto) {
        OrderTable orderTable =
                new OrderTable(new GuestNumber(tableDto.getNumberOfGuests()), tableDto.isEmpty(), null);
        OrderTable savedOrderTable = tableRepository.save(orderTable);
        return new TableDto(savedOrderTable);
    }

    public List<TableDto> list() {
        return tableRepository.findAll()
                .stream()
                .map(TableDto::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TableDto changeEmpty(Long orderTableId, TableDto table) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        savedOrderTable.setEmpty(table.isEmpty());
        return new TableDto(tableRepository.save(savedOrderTable));
    }

    @Transactional
    public TableDto changeNumberOfGuests(Long orderTableId, TableDto table) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeGuestNumber(new GuestNumber(table.getNumberOfGuests()));
        return new TableDto(tableRepository.save(savedOrderTable));
    }
}
