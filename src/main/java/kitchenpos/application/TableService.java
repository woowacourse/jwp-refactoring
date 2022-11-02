package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyResponse;
import kitchenpos.ui.jpa.dto.ordertable.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeNumberOfGuestsResponse;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableCreateResponse;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private TableServiceAssistant tableServiceAssistant;
    private OrderTableRepository orderTableRepository;
    private OrderService orderService;

    public TableService(TableServiceAssistant tableServiceAssistant,
                        OrderTableRepository orderTableRepository,
                        OrderService orderService) {
        this.tableServiceAssistant = tableServiceAssistant;
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
    }

    @Transactional
    public OrderTableCreateResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = new OrderTable(orderTableCreateRequest.getNumberOfGuests(), orderTableCreateRequest.isEmpty());
        orderTableRepository.save(orderTable);
        return new OrderTableCreateResponse(orderTable.getId(), null, orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    public List<OrderTableListResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(orderTable -> new OrderTableListResponse(orderTable.getId(), orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChangeEmptyResponse changeEmpty(Long orderTableId,
                                           ChangeEmptyRequest changeEmptyRequest) {
        OrderTable orderTable = tableServiceAssistant.findTable(orderTableId);
        if (orderService.isNotAllOrderFinish(orderTable)) {
            throw new IllegalArgumentException();
        }

        orderTable.changeEmpty(changeEmptyRequest.isEmpty());

        return new ChangeEmptyResponse(orderTableId, null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    @Transactional
    public ChangeNumberOfGuestsResponse changeNumberOfGuests(Long orderTableId,
                                                             ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest) {
        OrderTable orderTable = tableServiceAssistant.findTable(orderTableId);

        orderTable.changeNumberOfGuests(changeNumberOfGuestsRequest.getNumberOfGuests());
        return new ChangeNumberOfGuestsResponse(orderTableId, orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}
