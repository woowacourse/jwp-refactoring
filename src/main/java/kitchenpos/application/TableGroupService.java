package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final List<OrderTableDto> orderTableDtos = tableGroupDto.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableDtos) || orderTableDtos.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTableDtos.stream()
                                                       .map(OrderTableDto::getId)
                                                       .collect(Collectors.toList());

        final List<OrderTableDto> savedOrderTableDtos = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableDtos.size() != savedOrderTableDtos.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTableDto savedOrderTableDto : savedOrderTableDtos) {
            if (!savedOrderTableDto.isEmpty() || Objects.nonNull(savedOrderTableDto.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        tableGroupDto.setCreatedDate(LocalDateTime.now());

        final TableGroupDto savedTableGroupDto = tableGroupDao.save(tableGroupDto);

        final Long tableGroupId = savedTableGroupDto.getId();
        for (final OrderTableDto savedOrderTableDto : savedOrderTableDtos) {
            savedOrderTableDto.setTableGroupId(tableGroupId);
            savedOrderTableDto.setEmpty(false);
            orderTableDao.save(savedOrderTableDto);
        }
        savedTableGroupDto.setOrderTables(savedOrderTableDtos);

        return savedTableGroupDto;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTableDto> orderTableDtos = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTableDtos.stream()
                                                       .map(OrderTableDto::getId)
                                                       .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTableDto orderTableDto : orderTableDtos) {
            orderTableDto.setTableGroupId(null);
            orderTableDto.setEmpty(false);
            orderTableDao.save(orderTableDto);
        }
    }
}
