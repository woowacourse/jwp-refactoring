package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.ChangeNumberOfGuestRequest;
import kitchenpos.dto.OrderTableRequest;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        final OrderTable orderTable = request.toOrderTable();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        // TODO: 2023/10/17 ORDER REPOSITORY 만들고 나서 작업하기
//        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
//                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException("이미 주문이 진행 중이에요");
//        }

        savedOrderTable.updateEmptyStatus(request.isEmpty());
        return savedOrderTable;
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("없는 테이블이에요"));
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.updateNumberOfGuest(request.getNumberOfGuest());

        return savedOrderTable;
    }
}
