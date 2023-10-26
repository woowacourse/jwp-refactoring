package kitchenpos.table.service;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.service.dto.TableRequest;
import kitchenpos.table.service.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final TableRequest request) {
        final OrderTable orderTable = new OrderTable(
                null,
                request.getNumberOfGuests(),
                request.isEmpty()
        );
        return TableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return TableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateEmptyIfNoConstraints(request.isEmpty());
        return TableResponse.from(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블을 찾을 수 없습니다."));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return TableResponse.from(orderTable);
    }
}
