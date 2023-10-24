package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateEmpty(request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(orderTable);
    }
}
