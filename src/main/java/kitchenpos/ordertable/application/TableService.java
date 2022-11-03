package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.application.request.OrderTableCreateRequest;
import kitchenpos.ordertable.application.request.OrderTableUpdateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableDomainService tableDomainService;

    public TableService(final OrderTableRepository orderTableRepository, final TableDomainService tableDomainService) {
        this.orderTableRepository = orderTableRepository;
        this.tableDomainService = tableDomainService;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        return tableDomainService.changeEmpty(orderTableId, request.isEmpty());
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable 입니다."));
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("해당 OrderTable이 empty 상태 입니다.");
        }

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return savedOrderTable;
    }
}
