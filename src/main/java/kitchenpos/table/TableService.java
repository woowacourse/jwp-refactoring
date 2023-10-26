package kitchenpos.table;

import kitchenpos.order.request.TableCreateRequest;
import kitchenpos.order.request.TableUpdateRequest;
import kitchenpos.order.OrderDao;
import kitchenpos.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(TableCreateRequest request) {
        OrderTable orderTable = mapTOrderTable(request);

        return orderTableRepository.save(orderTable);
    }

    private OrderTable mapTOrderTable(TableCreateRequest request) {
        return OrderTable.of(request.getNumberOfGuests(), request.isEmpty());
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableUpdateRequest updateRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeStatus(updateRequest.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableUpdateRequest updateRequest) {
        final int numberOfGuests = updateRequest.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }
}
