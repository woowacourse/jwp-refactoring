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
    public Long create(final OrderTableCreateRequest request) {
        OrderTable orderTable = request.toEntity();
        OrderTable saved = orderTableRepository.save(orderTable);
        return saved.getId();
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public void changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable saved = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        saved.changeEmpty(request.isEmpty(), orderTableChangeEmptyService);
        orderTableRepository.save(saved);
    }

    @Transactional
    public void changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuests request) {
        final OrderTable saved = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        saved.changeNumberOfGuests(request.getNumberOfGuests());
        orderTableRepository.save(saved);
    }
}
