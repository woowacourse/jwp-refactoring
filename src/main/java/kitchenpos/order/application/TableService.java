package kitchenpos.order.application;

import kitchenpos.order.application.dto.request.OrderRequestAssembler;
import kitchenpos.order.application.dto.request.table.ChangeEmptyRequest;
import kitchenpos.order.application.dto.request.table.ChangeNumberOfGuestsRequest;
import kitchenpos.order.application.dto.request.table.OrderTableRequest;
import kitchenpos.order.application.dto.response.OrderResponseAssembler;
import kitchenpos.order.application.dto.response.OrderTableResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderRequestAssembler requestAssembler;
    private final OrderResponseAssembler responseAssembler;

    public TableService(final TableGroupRepository tableGroupRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderRepository orderRepository,
                        final OrderRequestAssembler requestAssembler,
                        final OrderResponseAssembler responseAssembler
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.requestAssembler = requestAssembler;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final var orderTable = requestAssembler.asOrderTable(request);
        final var savedOrderTable = orderTableRepository.save(orderTable);
        return responseAssembler.asOrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final var orderTables = orderTableRepository.findAll();
        return responseAssembler.asOrderTableResponses(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        return whenOrderTableExist(orderTableId, orderTable -> {
            validateOrderTableNotGrouped(orderTable);
            validateAllOrderTablesCompleted(orderTable.getId());

            final var isEmpty = request.isEmpty();
            orderTable.changeEmptyStatusTo(isEmpty);
        });
    }

    private void validateOrderTableNotGrouped(final OrderTable orderTable) {
        if (tableGroupRepository.existsByOrderTableIn(orderTable)) {
            throw new IllegalArgumentException("단체 지정된 테이블입니다.");
        }
    }

    private void validateAllOrderTablesCompleted(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("계산이 완료되지 않은 테이블입니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        return whenOrderTableExist(orderTableId, orderTable -> {
            final var numberOfGuests = request.getNumberOfGuests();
            orderTable.updateNumberOfGuests(numberOfGuests);
        });
    }

    private OrderTableResponse whenOrderTableExist(final Long orderTableId, final Consumer<OrderTable> ifOrderTableExist) {
        final OrderTable orderTable = asOrderTable(orderTableId);

        ifOrderTableExist.accept(orderTable);

        return responseAssembler.asOrderTableResponse(orderTable);
    }

    private OrderTable asOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }
}
