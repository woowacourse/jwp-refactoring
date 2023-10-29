package kitchenpos.ordertablegroup.application;

import kitchenpos.ordertablegroup.application.dto.OrderTableGroupCreateRequest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.ordertablegroup.domain.repository.OrderTableGroupRepository;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderTableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableGroupRepository orderTableGroupRepository;
    private final OrderTableGroupValidator orderTableGroupValidator;


    public OrderTableGroupService(
            final OrderTableRepository orderTableRepository,
            final OrderTableGroupRepository orderTableGroupRepository,
            final OrderTableGroupValidator orderTableGroupValidator
    ) {
        this.orderTableGroupValidator = orderTableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.orderTableGroupRepository = orderTableGroupRepository;
    }

    public Long create(final OrderTableGroupCreateRequest request) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.extractIds());
        orderTableGroupValidator.validate(orderTables, request.extractIds());
        final OrderTableGroup orderTableGroup = OrderTableGroup.of(LocalDateTime.now(), orderTables);
        final OrderTableGroup saveOrderTableGroup = orderTableGroupRepository.save(orderTableGroup);
        return saveOrderTableGroup.getId();
    }

    public void ungroup(final Long tableGroupId) {
        final OrderTableGroup orderTableGroup = orderTableGroupRepository.getById(tableGroupId);
        final OrderTables orderTables = orderTableGroup.getOrderTables();
        orderTableGroupValidator.validateOrderTableStatus(orderTables);
        orderTables.reset();
    }
}
