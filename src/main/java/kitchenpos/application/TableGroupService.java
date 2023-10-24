package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest.OrderTableRequest;
import kitchenpos.application.dto.tablegroup.TableGroupCreateResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private static final int MIN_NUMBER_OF_ORDER_TABLE = 2;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupCreateResponse create(final TableGroupCreateRequest request) {
        final List<OrderTableRequest> requestOrderTables = request.getOrderTables();
        checkOrderTableSize(requestOrderTables);

        final List<OrderTable> savedOrderTables = findOrderTablesByIds(requestOrderTables);

        checkValidOrderTablesSize(requestOrderTables, savedOrderTables);
        checkCanMakeTableGroup(savedOrderTables);

        final TableGroup tableGroup = new TableGroup(new OrderTables(savedOrderTables));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupCreateResponse.of(savedTableGroup);
    }

    private void checkOrderTableSize(final List<OrderTableRequest> requestOrderTables) {
        if (requestOrderTables.size() < MIN_NUMBER_OF_ORDER_TABLE) {
            throw new IllegalArgumentException("2개 이상의 주문 테이블을 그룹으로 만들 수 있습니다.");
        }
    }

    private List<OrderTable> findOrderTablesByIds(final List<OrderTableRequest> requestOrderTables) {
        final List<Long> orderTableIds = requestOrderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(toList());
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void checkValidOrderTablesSize(final List<OrderTableRequest> requestOrderTables,
                                           final List<OrderTable> savedOrderTables) {
        if (requestOrderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블의 정보가 올바르지 않습니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupById(tableGroupId);
        final List<OrderTable> findOrderTables = findOrderTablesByTableGroup(tableGroup);

        checkCompletedOrder(findOrderTables);

        final OrderTables orderTables = new OrderTables(findOrderTables);
        orderTables.ungroup();
    }

    private TableGroup findByTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다. id = " + tableGroupId));
    }

    private List<OrderTable> findOrderTablesByTableGroup(final TableGroup tableGroup) {
        return orderTableRepository.findAllByTableGroup(tableGroup);
    }

    private void checkCompletedOrder(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }
    }

    private void checkCanMakeTableGroup(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            checkFilled(savedOrderTable);
            checkGroupedAlready(savedOrderTable);
        }
    }

    private void checkFilled(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않습니다.");
        }
    }

    private void checkGroupedAlready(final OrderTable savedOrderTable) {
        if (savedOrderTable.hasTableGroup()) {
            throw new IllegalArgumentException("이미 테이블 그룹으로 등록되어 있습니다.");
        }
    }
}
