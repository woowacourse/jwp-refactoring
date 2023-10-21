package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.request.TableRequest;
import kitchenpos.application.dto.response.TableResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
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
        orderTable.checkTableGroupEmpty();

        // todo: orderTable이 order 리스트를 가지도록?
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("계산이 완료되지 않아 테이블의 상태를 바꿀 수 없습니다.");
        }

        orderTable.updateEmpty(request.isEmpty());
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
