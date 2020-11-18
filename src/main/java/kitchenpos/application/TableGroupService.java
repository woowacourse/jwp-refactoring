package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.exception.OrderNotCompleteException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupSizeException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.validator.OrderStatusValidate;

@Service
@Validated
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(@Valid final TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();

        final List<OrderTable> orderTables = orderTableRepository
            .findAllByIdIn(orderTableIds);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        final Long tableGroupId = savedTableGroup.getId();

        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.addToTableGroup(tableGroupId);
        }

        return savedTableGroup;
    }

    @OrderStatusValidate
    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
            tableGroupId);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}
