package kitchenpos.ordertable.service;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.infra.OrderTableRepository;
import kitchenpos.vo.NumberOfGuests;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final TableEmptyChangeService tableEmptyChangeService;
    private final OrderTableRepository orderTableRepository;

    public TableService(
            TableEmptyChangeService tableEmptyChangeService,
            OrderTableRepository orderTableRepository
    ) {
        this.tableEmptyChangeService = tableEmptyChangeService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(null, new NumberOfGuests(numberOfGuests), empty);
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, boolean empty) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        tableEmptyChangeService.execute(orderTable, empty);

        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(new NumberOfGuests(numberOfGuests));
        return orderTable;
    }
}
