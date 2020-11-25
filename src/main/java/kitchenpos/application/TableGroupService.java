package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequestDto;
import kitchenpos.dto.TableGroupResponseDto;
import kitchenpos.repository.OrderRepository;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao) {
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponseDto create(final TableGroupCreateRequestDto tableGroupCreateRequestDto) {
        final List<Long> orderTableIds = tableGroupCreateRequestDto.getOrderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("그룹 지정은 테이블의 수가 2보다 커야 합니다.");
        }

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블은 그룹지정할 수 없습니다.");
        }

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup());

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.group(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }

        return TableGroupResponseDto.from(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        final List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);

        for (Order order : orders) {
            if (!order.isComplete()) {
                throw new IllegalArgumentException("테이블의 주문이 아직 결제되지 않았습니다.");
            }
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }
}
