package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.validator.OrderTableValidator;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final TableGroupRepository tableGroupRepository,
                        final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final TableGroup tableGroup = tableGroupRepository.findById(orderTableRequest.getTableGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final OrderTable orderTable = new OrderTable(
                tableGroup,
                orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty()
        );
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTableValidator.validateCompletedOrderTable(orderTable);
        orderTable.setEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> all = orderTableRepository.findAll();
        return all.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }
}
