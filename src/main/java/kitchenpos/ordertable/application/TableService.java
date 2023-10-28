package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.request.OrderTableCreateRequest;
import kitchenpos.ordertable.request.TableChangeEmptyRequest;
import kitchenpos.ordertable.request.TableChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    private final OrderTableService orderTableService;

    public TableService(
            OrderTableRepository orderTableRepository,
            OrderTableService orderTableService
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuest(), request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, TableChangeEmptyRequest request) {
        OrderTable savedOrderTable = getById(orderTableId);
        orderTableService.checkOrderStatusInCookingOrMeal(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, TableChangeNumberOfGuestsRequest request) {
        OrderTable savedOrderTable = getById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public OrderTables findAllByTableIds(List<Long> orderTableIds) {
        return new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    @Transactional(readOnly = true)
    public void checkOrdersStatusInCookingOrMeal(OrderTables orderTables) {
        List<Long> orderTableIds = orderTables.getIds();
        orderTableService.checkOrdersStatusInCookingOrMeal(orderTableIds);
    }

    @Transactional(readOnly = true)
    public OrderTable getById(Long id) {
        return orderTableRepository.findById(id)
                                   .orElseThrow(IllegalArgumentException::new);
    }
}
