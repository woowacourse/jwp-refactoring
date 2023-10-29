package kitchenpos.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dto.request.ChangeEmptyRequest;
import kitchenpos.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.EmptyListException;
import kitchenpos.exception.GroupTableException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.validator.OrderStatusValidator;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.value.NumberOfGuests;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@ComponentScan(basePackages = "kitchenpos")
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderStatusValidator orderStatusValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final OrderStatusValidator orderStatusValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusValidator = orderStatusValidator;
    }

    public OrderTableResponse create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(request.getNumberOfGuests()));
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
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 테이블이 존재하지 않습니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new GroupTableException("그룹이 설정된 테이블의 상태는 변경 할 수 없습니다.");
        }

        // todo : 이벤트 처리 해결
        orderStatusValidator.validateById(orderTableId);

        savedOrderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(request.getNumberOfGuests());

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 테이블이 존재하지 않습니다."));

        if (savedOrderTable.isEmpty()) {
            throw new EmptyListException("비어있는 테이블의 손님 수를 변경할 수 없습니다.");
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
