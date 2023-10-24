package kitchenpos.ordertable.application;


import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.application.dto.TableEmptyRequest;
import kitchenpos.ordertable.application.dto.TableGuestRequest;
import kitchenpos.ordertable.application.dto.TableRequest;
import kitchenpos.ordertable.application.dto.TableResponse;
import kitchenpos.ordertable.domain.OrderStatusChecker;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderStatusChecker orderStatusChecker;

    public TableService(OrderTableRepository orderTableRepository, OrderStatusChecker orderStatusChecker) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusChecker = orderStatusChecker;
    }


    public TableResponse create(final TableRequest tableRequest) {
        OrderTable orderTable = tableRequest.toOrderTable();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return TableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return orderTableRepository.findAll().stream().map(TableResponse::from).collect(Collectors.toList());
    }

    public TableResponse changeEmpty(final Long orderTableId, final TableEmptyRequest tableEmptyRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderStatus(orderTableId);
        orderTable.changeEmpty(tableEmptyRequest.isEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return TableResponse.from(savedOrderTable);
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderStatusChecker.checkIncompleteOrders(orderTableId)) {
            throw new IllegalArgumentException();
        }
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableGuestRequest tableGuestRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumberOfGuests(tableGuestRequest.getNumberOfGuests());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return TableResponse.from(savedOrderTable);
    }

}
