package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableModifyRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = new OrderTable(orderTableCreateRequest.getNumberOfGuests(),
                orderTableCreateRequest.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableModifyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.findByOrderTableIdInAndOrderStatusIn(
                List.of(orderTableId), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)).size() > 0) {
            throw new IllegalArgumentException();
        }

        changeOrderTableEmpty(request, orderTable);

        return orderTable;
    }

    private void changeOrderTableEmpty(OrderTableModifyRequest request, OrderTable orderTable) {
        if (request.isEmpty()) {
            orderTable.empty();
            return;
        }
        orderTable.full();
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableModifyRequest orderTableModifyRequest) {
        final int numberOfGuests = orderTableModifyRequest.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return savedOrderTable;
    }
}
