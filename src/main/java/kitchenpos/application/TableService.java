package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao,
                        final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable create(final OrderTable orderTable) {
        OrderTable table = OrderTable.of(null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
        return orderTableRepository.save(table);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> readAll() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("이미 속해있는 table group이 있습니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateEmpty(orderTable.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }
}
