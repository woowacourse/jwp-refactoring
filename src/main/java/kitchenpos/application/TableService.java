package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
import kitchenpos.ui.dto.TableUpdateEmptyRequest;
import kitchenpos.ui.dto.TableUpdateGuestNumberRequest;
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
    public TableResponse create(final TableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return generateTableResponse(savedOrderTable);
    }

    public List<TableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(TableService::generateTableResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateEmptyUpdatable();
        validateMealFinished(orderTableId);
        savedOrderTable.updateEmpty(request.isEmpty());

        return generateTableResponse(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableUpdateGuestNumberRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateNotEmpty(savedOrderTable);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return generateTableResponse(savedOrderTable);
    }

    private static TableResponse generateTableResponse(final OrderTable savedOrderTable) {
        return new TableResponse(
                savedOrderTable.getId(),
                savedOrderTable.getTableGroupIdOrElseNull(),
                savedOrderTable.getNumberOfGuests(),
                savedOrderTable.isEmpty()
        );
    }

    private void validateNotEmpty(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMealFinished(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
