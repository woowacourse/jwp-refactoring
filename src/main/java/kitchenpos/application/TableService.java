package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.domain.table_group.TableGroupEvent;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.GuestStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table_group.TableGroupValidator;
import kitchenpos.domain.table_group.TableUnGroupEvent;
import kitchenpos.support.AggregateReference;
import kitchenpos.repositroy.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;
    private final TableGroupValidator tableGroupValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final OrderTableValidator orderTableValidator,
            final TableGroupValidator tableGroupValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final GuestStatus guestStatus = new GuestStatus(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(new OrderTable(guestStatus)));
    }

    @Transactional
    public OrderTableResponse changeEmpty(
            final Long orderTableId,
            final OrderTableChangeEmptyRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeEmpty(request.isEmpty(), orderTableValidator);
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeNumberOfGuestsRequest request
    ) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuest(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @TransactionalEventListener
    public void group(final TableGroupEvent request) {
        request.getOrderTableIds().stream()
                .map(AggregateReference::getId)
                .map(orderTableRepository::getById)
                .forEach(orderTable -> orderTable.group(request.getTableGroupId()));
    }

    @TransactionalEventListener
    public void ungroup(final TableUnGroupEvent request) {
        orderTableRepository.findByTableGroupId(request.getTableGroupId())
                .forEach(it -> it.unGroup(tableGroupValidator));
    }
}
