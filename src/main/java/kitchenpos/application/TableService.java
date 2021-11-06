package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrdersRepository;
import kitchenpos.ui.dto.TableRequest;
import kitchenpos.ui.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrdersRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrdersRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public TableResponse create(final TableRequest tableRequest) {
        OrderTable newTable = orderTableRepository.save(tableRequest.toOrderTable());

        return TableResponse.from(newTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> findAll() {
        List<OrderTable> tables = orderTableRepository.findAll();

        return tables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    public TableResponse changeEmpty(final Long orderTableId, final TableRequest tableRequest) {
        OrderTable table = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));
        changeEmpty(orderTableId, tableRequest, table);

        return TableResponse.from(orderTableRepository.save(table));
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest tableRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));
        orderTable.changeNumberOfGuests(tableRequest.getNumberOfGuest());

        return TableResponse.from(orderTableRepository.save(orderTable));
    }

    private void changeEmpty(Long orderTableId, TableRequest tableRequest, OrderTable table) {
        List<Orders> notCompletedOrders = orderRepository.findAllByOrderTableId(orderTableId).stream()
                .filter(order -> !order.isCompleted())
                .collect(Collectors.toList());

        checkExistsNotCompletedOrder(notCompletedOrders);
        table.changeEmpty(tableRequest.getEmpty());
    }

    private void checkExistsNotCompletedOrder(List<Orders> notCompletedOrders) {
        if (!notCompletedOrders.isEmpty()) {
            throw new IllegalArgumentException("조리중이거나, 식사중인 테이블이 존재합니다.");
        }
    }
}
