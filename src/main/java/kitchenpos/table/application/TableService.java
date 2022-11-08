package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.ui.dto.ordertable.ChangeEmptyRequest;
import kitchenpos.table.ui.dto.ordertable.ChangeEmptyResponse;
import kitchenpos.table.ui.dto.ordertable.ChangeNumberOfGuestsRequest;
import kitchenpos.table.ui.dto.ordertable.ChangeNumberOfGuestsResponse;
import kitchenpos.table.ui.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.ordertable.OrderTableCreateResponse;
import kitchenpos.table.ui.dto.ordertable.OrderTableListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private TableServiceAssistant tableServiceAssistant;
    private OrderTableRepository orderTableRepository;
    private TableRule tableRule;

    public TableService(TableServiceAssistant tableServiceAssistant,
                        OrderTableRepository orderTableRepository,
                        TableRule tableRule) {
        this.tableServiceAssistant = tableServiceAssistant;
        this.orderTableRepository = orderTableRepository;
        this.tableRule = tableRule;
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
        if (tableRule.unableToChangeEmpty(orderTable)) {
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
