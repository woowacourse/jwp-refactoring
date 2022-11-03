package kitchenpos.table.application;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableRepository.save(new OrderTable(null,
                null,
                request.getNumberOfGuests(),
                request.isEmpty()));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        checkOrderIsInProgress(orderTableId);
        orderTable.updateEmpty(request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    private void checkOrderIsInProgress(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.collectInProgress())) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(orderTable);
    }
}
