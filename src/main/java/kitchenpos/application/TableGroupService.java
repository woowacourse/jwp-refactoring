package kitchenpos.application;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.entity.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.service.TableGroupCreateService;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupCreateService tableGroupCreateService;

    public TableGroupService(final OrderDao orderDao,
            final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository,
            TableGroupCreateService tableGroupCreateService) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupCreateService = tableGroupCreateService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = request.toEntity();
        TableGroup saved = tableGroupRepository.save(tableGroup.create(tableGroupCreateService));
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
                tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            // TODO: 2020/11/01 OrderTable 해결
            // orderTable.setTableGroupId(null);
            // orderTable.setEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }
}
