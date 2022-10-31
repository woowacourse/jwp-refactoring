package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import kitchenpos.repository.order.TableGroupRepository;
import kitchenpos.specification.TableGroupSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupSpecification tableGroupSpecification;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableGroupSpecification tableGroupSpecification) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupSpecification = tableGroupSpecification;
    }

    @Transactional
    public TableGroup create(TableGroupRequest request) {

        List<Long> requestOrderTableIds = extractOrderTableIds(request);

        List<OrderTable> savedOrderTables =
                orderTableRepository.findWithTableGroupByIdIn(requestOrderTableIds);

        tableGroupSpecification.validateCreate(request, savedOrderTables);

        TableGroup tableGroup = new TableGroup(request.getId(), LocalDateTime.now(), savedOrderTables);
        tableGroup.validate();
        tableGroup.initCurrentDateTime();
        tableGroup.changeStatusNotEmpty();

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return savedTableGroup;
    }

    private List<Long> extractOrderTableIds(TableGroupRequest request) {

        return request.getOrderTables().stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByIdInAndStatusIn(orderTableIds, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 주문 테이블이 있는 경우 그룹을 해제할 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }
}
