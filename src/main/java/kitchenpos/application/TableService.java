package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.request.TableCreateRequest;
import kitchenpos.ui.request.TableEmptyUpdateRequest;
import kitchenpos.ui.request.TableGuestUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class TableService {

    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable create(final TableCreateRequest tableCreateRequest) {
        OrderTable orderTable = new OrderTable(null, tableCreateRequest.getNumberOfGuests(), tableCreateRequest.getEmpty());

        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, TableEmptyUpdateRequest tableEmptyUpdateRequest) {
        OrderTable savedOrderTable = getOrderTable(orderTableId);

        savedOrderTable.checkTableGroup();

        checkIfOrderIsNotCompleted(orderTableId);

        savedOrderTable.changeEmpty(tableEmptyUpdateRequest.getEmpty());

        return savedOrderTable;
    }

    private void checkIfOrderIsNotCompleted(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, TableGuestUpdateRequest tableGuestUpdateRequest) {
        OrderTable savedOrderTable = getOrderTable(orderTableId);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuest(tableGuestUpdateRequest.getNumberOfGuests());

        return savedOrderTable;
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
