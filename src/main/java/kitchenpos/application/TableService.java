package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Repository.OrderRepository;
import kitchenpos.Repository.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.request.TableChangeEmptyRequest;
import kitchenpos.ui.request.TableChangeNumberOfGuestsRequest;
import kitchenpos.ui.response.OrderTableResponse;
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
        OrderTable orderTable = request.toEntity();
        orderTable.setId(null);
        orderTable.clearTableGroup();

        OrderTable save = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(save);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final TableChangeEmptyRequest request) {
        OrderTable orderTable = new OrderTable(orderTableId, null, 0, request.getEmpty());
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.validateTableGroup();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        OrderTable save = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.from(save);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final TableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        OrderTable orderTable = new OrderTable(orderTableId, null, request.getNumberOfGuests(), savedOrderTable.isEmpty());
        orderTable.validateGuest();

        savedOrderTable.validateEmpty();
        savedOrderTable.setNumberOfGuests(orderTable.getNumberOfGuests());

        OrderTable save = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.from(save);
    }
}
