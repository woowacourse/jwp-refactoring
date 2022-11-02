package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.event.CheckTableCanChangeEvent;
import kitchenpos.table.application.request.ChangeNumOfTableGuestsRequest;
import kitchenpos.table.application.request.ChangeOrderTableEmptyRequest;
import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableService(OrderTableRepository orderTableRepository,
                        ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return new OrderTableResponse(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(ChangeOrderTableEmptyRequest request) {
        OrderTable orderTable = findOrderTable(request.getOrderTableId());
        applicationEventPublisher.publishEvent(new CheckTableCanChangeEvent(List.of(request.getOrderTableId())));

        orderTable.changeEmptyStatus(request.isEmpty());

        return new OrderTableResponse(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(ChangeNumOfTableGuestsRequest request) {
        OrderTable orderTable = findOrderTable(request.getOrderTableId());

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }
}
