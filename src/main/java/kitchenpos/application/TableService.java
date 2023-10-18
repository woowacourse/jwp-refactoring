package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableCreationRequest;
import kitchenpos.dto.TableEmptyUpdateRequest;
import kitchenpos.dto.TableNumberOfGuestsUpdateRequest;
import kitchenpos.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableResponse create(TableCreationRequest request) {
        OrderTable orderTable = OrderTable.createWithoutTableGroup(request.getNumberOfGuests(), request.getEmpty());

        orderTableRepository.save(orderTable);

        return TableResponse.from(orderTable);
    }

    public List<TableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(Long orderTableId, TableEmptyUpdateRequest request) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        validateChangeableEmpty(orderTableId);

        orderTable.changeEmpty(request.getEmpty());

        return TableResponse.from(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, TableNumberOfGuestsUpdateRequest request) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return TableResponse.from(orderTable);
    }

    private void validateChangeableEmpty(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("Completion 상태가 아닌 주문 테이블은 주문 가능 여부를 변경할 수 없습니다.");
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 주문 테이블이 존재하지 않습니다."));
    }
}
