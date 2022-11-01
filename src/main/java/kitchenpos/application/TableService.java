package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.TableRepository;
import kitchenpos.specification.TableSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final TableSpecification tableSpecification;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public TableService(TableSpecification tableSpecification,
                        OrderRepository orderRepository,
                        TableRepository tableRepository) {
        this.tableSpecification = tableSpecification;
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderTable create(OrderTableRequest request) {

        OrderTable orderTable = request.toDomain();

        orderTable.clearKeys();

        return tableRepository.save(orderTable);
    }

    public List<OrderTable> list() {

        return tableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableRequest orderTableRequest) {

        final OrderTable orderTable =
                tableRepository.findWithTableGroupById(orderTableId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 ID입니다."));

        orderTable.validateChangeEmptyStatus();

        boolean empty = orderTableRequest.isEmpty();

        orderTable.changeEmptyStatus(empty);

        return orderTable;
    }


    @Transactional
    public OrderTable changeNumberOfGuests_asis(Long orderTableId,
                                           OrderTableRequest orderTableRequest) {

        final OrderTable orderTable = orderTableRequest.toDomain();

        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 음수가 될 수 없습니다.");
        }

        final OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("변경 요청한 주문테이블이 존재하지 않습니다."));

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return tableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId,
                                           OrderTableRequest request) {

        tableSpecification.validateChangeNumberOfGuests(request);

        final OrderTable orderTable = tableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("변경 요청한 주문테이블이 존재하지 않습니다."));

        orderTable.validateChangeNumberOfGuests();

        orderTable.setNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }
}
