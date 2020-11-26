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
    public TableResponse changeEmpty(final Long tableId,
        final TableChangeEmptyRequest tableChangeEmptyRequest) {
        Table table = findById(tableId);
        validationOrderStatusCompletion(tableId);
        table.changeEmpty(tableChangeEmptyRequest.isEmpty());

        return TableResponse.of(table);
    }

    private void validationOrderStatusCompletion(Long tableId) {
        if (orderRepository.existsByTable_IdAndOrderStatusIn(tableId,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("진행중인 주문건이 있습니다.");
        }
    }

    private Table findById(Long tableId) {
        return tableRepository.findById(tableId)
            .orElseThrow(
                () -> new IllegalArgumentException("ID에 해당하는 Table이 없습니다. {" + tableId + "}"));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long tableId,
        TableChangeNumberOfGuestsRequest tableChangeNumberOfGuestRequest) {
        int numberOfGuests = tableChangeNumberOfGuestRequest.getNumberOfGuests();

        Table table = findById(tableId);
        table.changeNumberOfGuests(numberOfGuests);

        return TableResponse.of(table);
    }
}
