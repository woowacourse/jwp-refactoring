package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateTableDto;
import kitchenpos.application.dto.request.EmptyTableDto;
import kitchenpos.application.dto.response.TableDto;
import kitchenpos.application.dto.request.UpdateGuestNumberDto;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableDto create(final CreateTableDto createTableDto) {
        OrderTable orderTable = new OrderTable(createTableDto.getNumberOfGuests(), createTableDto.getEmpty());
        return TableDto.of(orderTableRepository.save(orderTable));
    }

    public List<TableDto> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(EmptyTableDto emptyTableDto) {
        Long orderTableId = emptyTableDto.getOrderTableId();
        final OrderTable savedOrderTable = orderTableRepository.get(orderTableId);
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getOngoingStatuses())) {
            throw new IllegalArgumentException("식사 중인 테이블은 비울 수 없습니다.");
        }
        savedOrderTable.changeEmpty(emptyTableDto.getEmpty());
        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public TableDto changeNumberOfGuests(UpdateGuestNumberDto updateGuestNumberDto) {
        final Long orderTableId = updateGuestNumberDto.getOrderTableId();
        final int numberOfGuests = updateGuestNumberDto.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.get(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return TableDto.of(orderTableRepository.save(savedOrderTable));
    }
}
