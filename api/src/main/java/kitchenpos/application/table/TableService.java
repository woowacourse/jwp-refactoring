package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.repository.OrderTableRepository;
import kitchenpos.dto.table.TableRequest;
import kitchenpos.dto.table.TableResponse;
import kitchenpos.event.OrderStatusCheckEventPublisher;
import kitchenpos.exception.NonExistentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderStatusCheckEventPublisher orderStatusCheckEventPublisher;

    public TableService(OrderTableRepository orderTableRepository, OrderStatusCheckEventPublisher orderStatusCheckEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusCheckEventPublisher = orderStatusCheckEventPublisher;
    }

    @Transactional
    public TableResponse create(final TableRequest tableRequest) {
        return TableResponse.from(orderTableRepository.save(tableRequest.toOrderTable()));
    }

    public List<TableResponse> list() {
        return TableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NonExistentException("주문테이블을 찾을 수 없습니다."));
        savedOrderTable.checkOrderStatus(orderStatusCheckEventPublisher);
        savedOrderTable.changeEmpty(tableRequest.isEmpty());
        return TableResponse.from(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest tableRequest) {
        int numberOfGuests = tableRequest.getNumberOfGuests();
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NonExistentException("주문테이블을 찾을 수 없습니다."));
        savedOrderTable.addNumberOfGuests(numberOfGuests);
        return TableResponse.from(savedOrderTable);
    }
}
