package kitchenpos.infrastructure.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableDao;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {

    private final OrderTableDao orderTableDao;

    public OrderTableRepositoryImpl(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public OrderTable get(final Long id) {
        final var orderTable = orderTableDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return new OrderTable(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    @Override
    public OrderTable add(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    @Override
    public List<OrderTable> addAll(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(this::add)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> getAll() {
        return orderTableDao.findAll();
    }
}
