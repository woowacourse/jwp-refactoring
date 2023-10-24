package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao) {
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
                                                       .collect(toList());

        final List<OrderTableDto> savedOrderTableDtos = orderTableDao.findAllByIdIn(orderTableIds)
                                                                     .stream()
                                                                     .map(OrderTableDto::from)
                                                                     .collect(toList());

        if (orderTableDtos.size() != savedOrderTableDtos.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTableDto savedOrderTableDto : savedOrderTableDtos) {
            if (!savedOrderTableDto.isEmpty() || Objects.nonNull(
                savedOrderTableDto.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        tableGroupDto.setCreatedDate(LocalDateTime.now());

        TableGroup savedTableGroup = tableGroupDao.save(toEntity(tableGroupDto));
        final TableGroupDto savedTableGroupDto = TableGroupDto.from(savedTableGroup);

        final Long tableGroupId = savedTableGroupDto.getId();
        for (final OrderTableDto savedOrderTableDto : savedOrderTableDtos) {
            savedOrderTableDto.setTableGroupId(tableGroupId);
            savedOrderTableDto.setEmpty(false);
            orderTableDao.save(toEntity(savedOrderTableDto));
        }
        savedTableGroupDto.setOrderTables(savedOrderTableDtos);

        return savedTableGroupDto;
    }

    private TableGroup toEntity(TableGroupDto tableGroupDto) {
        List<OrderTable> orderTables = tableGroupDto.getOrderTables().stream()
                                                    .map(this::toEntity)
                                                    .collect(toList());
        return new TableGroup(
            tableGroupDto.getId(),
            tableGroupDto.getCreatedDate(),
            orderTables
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTableDto> orderTableDtos = orderTableDao.findAllByTableGroupId(tableGroupId)
                                                                .stream()
                                                                .map(OrderTableDto::from)
                                                                .collect(toList());

        final List<Long> orderTableIds = orderTableDtos.stream()
                                                       .map(OrderTableDto::getId)
                                                       .collect(toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTableDto orderTableDto : orderTableDtos) {
            orderTableDto.setTableGroupId(null);
            orderTableDto.setEmpty(false);
            orderTableDao.save(toEntity(orderTableDto));
        }
    }

    private OrderTable toEntity(OrderTableDto orderTableDto) {
        Long tableGroupId = orderTableDto.getTableGroupId();
        TableGroup tableGroup = null;
        if (tableGroupId != null) {
            tableGroup = tableGroupDao.findById(tableGroupId)
                                      .orElseThrow(IllegalArgumentException::new);
        }

        return new OrderTable(
            orderTableDto.getId(),
            tableGroup,
            orderTableDto.getNumberOfGuests(),
            orderTableDto.isEmpty()
        );
    }
}
