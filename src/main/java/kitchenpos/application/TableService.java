package kitchenpos.application;

import java.util.Arrays;
import kitchenpos.application.dtos.GuestNumberRequest;
import kitchenpos.application.dtos.OrderTableRequest;
import kitchenpos.application.dtos.OrderTableResponse;
import kitchenpos.application.dtos.OrderTableResponses;
import kitchenpos.application.dtos.TableEmptyRequest;
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
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = orderTableWith(request);
        return new OrderTableResponse(orderTableRepository.save(orderTable));
    }

    public OrderTableResponses list() {
        return new OrderTableResponses(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final TableEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.checkTableGroupId();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateEmpty(request.getEmpty());

        return new OrderTableResponse(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final GuestNumberRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.checkValidity();
        savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable orderTableWith(OrderTableRequest request) {
        return OrderTable.builder()
                .id(request.getId())
                .build();
    }

}
