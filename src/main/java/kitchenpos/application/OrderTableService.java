package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.service.OrderTableChangeEmptyService;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableChangeEmptyService orderTableChangeEmptyService;

    public OrderTableService(final OrderTableRepository orderTableRepository,
            OrderTableChangeEmptyService orderTableChangeEmptyService) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableChangeEmptyService = orderTableChangeEmptyService;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = request.toEntity();
        OrderTable saved = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(saved);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable saved = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        saved.changeEmpty(request.isEmpty(), orderTableChangeEmptyService);
        OrderTable changed = orderTableRepository.save(saved);
        return OrderTableResponse.of(changed);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuests request) {
        final OrderTable saved = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        saved.changeNumberOfGuests(request.getNumberOfGuests());
        OrderTable changed = orderTableRepository.save(saved);
        return OrderTableResponse.of(changed);
    }
}
