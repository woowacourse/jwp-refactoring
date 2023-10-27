package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyStatusUpdateRequest;
import kitchenpos.table.dto.TableNumberOfGuestsUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Transactional
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable create(final TableCreateRequest request) {
        final OrderTable orderTable = OrderTable.create(request.getNumberOfGuests(), request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final TableEmptyStatusUpdateRequest request) {
        final OrderTable orderTable = getOrderTable(orderTableId);

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리중 또는 식사중인 주문 테이블은 빈 테이블로 변경할 수 없습니다.");
        }

        orderTable.changeEmpty(request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableNumberOfGuestsUpdateRequest request) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTable;
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }
}
