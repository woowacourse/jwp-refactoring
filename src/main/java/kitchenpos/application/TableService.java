package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.OrderStatus;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.value.NumberOfGuests;
import kitchenpos.dto.request.table.ChangeEmptyRequest;
import kitchenpos.dto.request.table.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.table.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.EmptyListException;
import kitchenpos.exception.GroupTableException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.exception.InvalidOrderStateException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(new NumberOfGuests(request.getNumberOfGuests()))
                .build();

        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {

        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(()-> new NoSuchDataException("해당하는 id의 테이블이 존재하지 않습니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new GroupTableException("그룹이 설정된 테이블의 상태는 변경 할 수 없습니다.");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStateException("조리 중이거나 식사 중인 테이블의 상태는 변경 할 수 없습니다.");
        }

        final OrderTable orderTable = OrderTable.builder()
                .id(orderTableId)
                .numberOfGuests(savedOrderTable.getNumberOfGuests())
                .empty(request.getEmpty())
                .build();

        final OrderTable updatedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(updatedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(request.getNumberOfGuests());

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(()->new NoSuchDataException("해당하는 id의 테이블이 존재하지 않습니다."));

        if (savedOrderTable.isEmpty()) {
            throw new EmptyListException("비어있는 테이블의 손님 수를 변경할 수 없습니다.");
        }

        final OrderTable orderTable = OrderTable.builder()
                .id(orderTableId)
                .numberOfGuests(numberOfGuests)
                .build();
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }
}
