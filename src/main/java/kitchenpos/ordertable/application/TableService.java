package kitchenpos.ordertable.application;

import kitchenpos.ordertable.application.dto.ChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.application.dto.OrderTableRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableChangeEmptyEvent;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderTable create(OrderTableRequest orderTableRequest) {

        OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuest(), orderTableRequest.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, ChangeEmptyRequest changeEmptyRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        applicationEventPublisher.publishEvent(new OrderTableChangeEmptyEvent(orderTableId));

        savedOrderTable.changeEmptyStatus(changeEmptyRequest.isEmpty());


        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(changeNumberOfGuestsRequest.getNumberOfGuests());

        return orderTableRepository.save(savedOrderTable);
    }
}
