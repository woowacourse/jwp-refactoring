package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableUpdateEmptyRequest;
import kitchenpos.dto.request.TableUpdateGuestRequest;
import kitchenpos.dto.response.TableResponse;
import kitchenpos.order.repository.OrderRepository;
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
    public Long create(final TableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return savedOrderTable.getId();
    }

    public List<TableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableUpdateEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        orderTable.checkTableGroupsEmpty();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 아직 완료상태가 아닙니다.");
        }

        orderTable.changeEmpty(request.isEmpty());
        final OrderTable updatedTable = orderTableRepository.save(orderTable);
        return TableResponse.from(updatedTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableUpdateGuestRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));

        orderTable.checkEmptyIsFalse();
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        final OrderTable updatedTable = orderTableRepository.save(orderTable);
        return TableResponse.from(updatedTable);
    }
}
