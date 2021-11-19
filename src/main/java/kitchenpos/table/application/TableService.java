package kitchenpos.table.application;

import kitchenpos.event.OrderStatusCheckEventPublisher;
import kitchenpos.exception.NonExistentException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.ui.dto.TableRequest;
import kitchenpos.table.ui.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
