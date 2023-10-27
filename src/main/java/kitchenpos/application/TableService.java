package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.table.TableEmptyChangeRequest;
import kitchenpos.application.dto.table.TableGuestChangeRequest;
import kitchenpos.application.dto.table.TableRequest;
import kitchenpos.application.dto.table.TableResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    public TableResponse create(final TableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return TableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
            .map(TableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableEmptyChangeRequest request) {
        final OrderTable orderTable = findById(orderTableId);

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        orderTable.updateEmpty(request.isEmpty());

        return TableResponse.from(orderTable);
    }

    private OrderTable findById(final Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableGuestChangeRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경하려는 손님 수는 0보다 작을 수 없습니다.");
        }

        final OrderTable orderTable = findById(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return TableResponse.from(orderTable);
    }
}
