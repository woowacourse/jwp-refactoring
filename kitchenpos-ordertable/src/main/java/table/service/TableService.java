package table.service;

import exception.EmptyListException;
import exception.GroupTableException;
import exception.NoSuchDataException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import table.domain.OrderTable;
import table.dto.OrderStatusValidateByIdEvent;
import table.dto.request.ChangeEmptyRequest;
import table.dto.request.ChangeNumberOfGuestsRequest;
import table.dto.request.CreateOrderTableRequest;
import table.dto.response.OrderTableResponse;
import table.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import value.NumberOfGuests;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
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
        publisher.publishEvent(new OrderStatusValidateByIdEvent(savedOrderTable.getId()));

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
