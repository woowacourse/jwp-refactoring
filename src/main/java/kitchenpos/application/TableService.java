package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.Table;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.TableRepository;
import kitchenpos.ui.dto.TableChangeEmptyRequest;
import kitchenpos.ui.dto.TableChangeNumberOfGuestsRequest;
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
    public TableResponse changeEmpty(final Long orderTableId,
        final TableChangeEmptyRequest tableChangeEmptyRequest) {
        final Table savedTable = tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (orderRepository.existsByTable_IdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        savedTable.changeEmpty(tableChangeEmptyRequest.isEmpty());

        return TableResponse.of(savedTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId,
        final TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestRequest) {
        final int numberOfGuests = tableChangeNumberOfGuestRequest.getNumberOfGuests();

        final Table savedTable = tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        savedTable.changeNumberOfGuests(numberOfGuests);

        return TableResponse.of(savedTable);
    }
}
