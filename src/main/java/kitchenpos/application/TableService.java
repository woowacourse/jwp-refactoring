package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableChangeEmptyDto;
import kitchenpos.dto.TableChangeNumberOfGuestsDto;
import kitchenpos.dto.TableCreateDto;
import kitchenpos.dto.TableDto;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
    public TableDto create(final TableCreateDto request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        orderTableRepository.save(orderTable);

        return TableDto.toDto(orderTable);
    }

    public List<TableDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(TableDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(final Long orderTableId, final TableChangeEmptyDto request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

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
