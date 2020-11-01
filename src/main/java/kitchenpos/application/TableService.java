package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public TableService(final OrderRepository orderRepository,
        final TableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest orderTableRequest) {
        Table table = orderTableRequest.toOrderTable();
        Table savedTable = tableRepository.save(table);

        return TableResponse.of(savedTable);
    }

    public List<TableResponse> list() {

        List<Table> foundTables = tableRepository.findAll();
        return TableResponse.listOf(foundTables);
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final Table table) {
        final Table savedOrderTable = tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(table.isEmpty());
        Table savedTable = tableRepository.save(savedOrderTable);

        return TableResponse.of(savedTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId,
        final Table table) {
        final int numberOfGuests = table.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final Table savedOrderTable = tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        Table savedTable = tableRepository.save(savedOrderTable);
        return TableResponse.of(savedTable);
    }
}
