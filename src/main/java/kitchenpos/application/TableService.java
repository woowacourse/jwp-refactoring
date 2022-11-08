package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderDetail;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.repository.OrderDetailRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderDetailRepository orderDetailRepository;

    public TableService(OrderTableRepository orderTableRepository, OrderDetailRepository orderDetailRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.getEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request) {
        OrderDetail orderDetail = orderDetailRepository.findByOrderTableId(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
        orderDetail.changeEmpty(request.getEmpty());

        return orderDetail.getOrderTable();
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, OrderTableChangeNumberOfGuestsRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
    }
}
