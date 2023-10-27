package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.domain.table_group.CreateTableGroupEvent;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.GuestStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table_group.TableGroupValidator;
import kitchenpos.domain.table_group.DeleteTableGroupEvent;
import kitchenpos.support.AggregateReference;
import kitchenpos.repositroy.OrderTableRepository;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;
    private final TableGroupValidator tableGroupValidator;

    @Autowired
    private EntityManager em;

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

    @EventListener
    @Transactional
    public void group(final CreateTableGroupEvent request) {
        request.getOrderTableIds().stream()
                .map(AggregateReference::getId)
                .map(orderTableRepository::getById)
                .forEach(orderTable -> orderTable.group(request.getTableGroupId()));
    }

    @EventListener
    @Transactional
    public void ungroup(final DeleteTableGroupEvent request) {
        orderTableRepository.findByTableGroupId(request.getTableGroupId().getId())
                .forEach(it -> it.unGroup(tableGroupValidator));
    }
}
