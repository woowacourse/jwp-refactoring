package kitchenpos.application.table;

import java.util.stream.Collectors;
import kitchenpos.application.table.dto.request.CreateTableDto;
import kitchenpos.application.table.dto.request.EmptyTableDto;
import kitchenpos.application.table.dto.response.TableDto;
import kitchenpos.application.table.dto.request.UpdateGuestNumberDto;
import kitchenpos.core.domain.order.OrderStatus;
import kitchenpos.core.repository.order.OrderRepository;
import kitchenpos.core.repository.table.OrderTableRepository;
import kitchenpos.core.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional(readOnly = true)
    public List<TableDto> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
    }

    public TableDto create(final CreateTableDto createTableDto) {
        OrderTable orderTable = new OrderTable(createTableDto.getNumberOfGuests(), createTableDto.getEmpty());
        return TableDto.of(orderTableRepository.save(orderTable));
    }

    public TableDto changeEmpty(EmptyTableDto emptyTableDto) {
        Long orderTableId = emptyTableDto.getOrderTableId();
        final OrderTable savedOrderTable = orderTableRepository.get(orderTableId);
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getOngoingStatuses())) {
            throw new IllegalArgumentException("식사 중인 테이블은 비울 수 없습니다.");
        }
        savedOrderTable.changeEmpty(emptyTableDto.getEmpty());
        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }

    public TableDto changeNumberOfGuests(UpdateGuestNumberDto updateGuestNumberDto) {
        final Long orderTableId = updateGuestNumberDto.getOrderTableId();
        final int numberOfGuests = updateGuestNumberDto.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.get(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }
}
