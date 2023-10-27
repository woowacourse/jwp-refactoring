package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.event.OrderCheckEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.TableCreateRequest;
import kitchenpos.table.dto.request.TableUpdateEmptyRequest;
import kitchenpos.table.dto.request.TableUpdateGuestRequest;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    private final ApplicationEventPublisher publisher;


    public TableService(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    @Transactional
    public Long create(final TableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return savedOrderTable.getId();
    }

    public List<TableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableUpdateEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        orderTable.checkTableGroupsEmpty();

        publisher.publishEvent(new OrderCheckEvent(orderTableId));

        orderTable.changeEmpty(request.isEmpty());
        final OrderTable updatedTable = orderTableRepository.save(orderTable);
        return TableResponse.from(updatedTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableUpdateGuestRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));

        orderTable.checkEmptyIsFalse();
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        final OrderTable updatedTable = orderTableRepository.save(orderTable);
        return TableResponse.from(updatedTable);
    }
}
