package application;

import application.dto.OrderTableChangeEmptyRequest;
import application.dto.OrderTableChangeNumberOfGuestsRequest;
import application.dto.OrderTableCreateRequest;
import application.dto.OrderTableResponse;
import domain.CreateTableGroupEvent;
import domain.DeleteTableGroupEvent;
import domain.GuestStatus;
import domain.OrderTable;
import domain.OrderTableValidator;
import domain.TableGroupValidator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.OrderTableRepository;

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

    @EventListener
    @Transactional
    public void group(final CreateTableGroupEvent request) {
        request.getOrderTableIds().stream()
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
