package kitchenpos.order.service;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.service.dto.TableRequest;
import kitchenpos.order.service.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableResponse create(final TableRequest request) {
        final OrderTable orderTable = new OrderTable(
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
        orderTable.updateEmptyIfNoConstraints(request.isEmpty(), orderTableValidator);
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
