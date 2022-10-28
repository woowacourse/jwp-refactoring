package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.OrderTableGroupingSizeException;
import kitchenpos.exception.OrderTableUnableUngroupingStatusException;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao,
                             TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest tableGroupCreateRequest) {
        validateOrderTablesSize(tableGroupCreateRequest.getOrderTables());

        List<Long> orderTableIds = getOrderTableIds1(tableGroupCreateRequest.getOrderTables());
        return tableGroupRepository.save(orderTableIds);
    }

    private void validateOrderTablesSize(List<OrderTableIdDto> orderTableIdDtos) {
        if (CollectionUtils.isEmpty(orderTableIdDtos) || orderTableIdDtos.size() < 2) {
            throw new OrderTableGroupingSizeException();
        }
    }

    private List<Long> getOrderTableIds1(List<OrderTableIdDto> orderTableIdDtos) {
        return orderTableIdDtos.stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderTablesStatus(orderTables);

        tableGroupRepository.ungroup(orderTables);
    }

    private void validateOrderTablesStatus(List<OrderTable> orderTables) {
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderTableUnableUngroupingStatusException();
        }
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
