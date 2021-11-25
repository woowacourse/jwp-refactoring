package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.ui.dto.request.OrderTableChangeGuestRequest;
import kitchenpos.table.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(
            OrderTableRepository orderTableRepository,
            TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        return OrderTableResponse.create(orderTableRepository.save(orderTableCreateRequest.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::create)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(
            final Long orderTableId,
            final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest
    ) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("orderTableId : " + orderTableId + "는 존재하지 않습니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("orderTableId : " + orderTableId + "는 테이블 그룹에 속해 있어 상태 변경이 불가능합니다.");
        }

        savedOrderTable.changeEmpty(orderTableChangeEmptyRequest.getEmpty(), tableValidator);

        return OrderTableResponse.create(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeGuestRequest orderTableChangeGuestRequest
    ) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("orderTableId : " + orderTableId + "는 존재하지 않습니다."));

        final NumberOfGuests numberOfGuests = NumberOfGuests
                .create(orderTableChangeGuestRequest.getNumberOfGuests());

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있으면 인원 수 변경이 불가능합니다.");
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.create(savedOrderTable);
    }
}
