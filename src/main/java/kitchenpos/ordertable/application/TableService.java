package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.application.dto.ChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.application.dto.OrderTableRequest;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    private final TableGroupRepository tableGroupRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
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

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 주문의 상태를 변경할 수 없습니다.");
        }

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
