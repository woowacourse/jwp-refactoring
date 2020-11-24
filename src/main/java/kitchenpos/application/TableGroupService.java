package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final TableDao tableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final TableDao tableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> tableIds = Optional.ofNullable(request.getTableIds())
            .orElseThrow(() -> new IllegalArgumentException("그룹지을 테이블들을 지정해주지 않으셨습니다."));
        final List<Table> tables = tableDao.findAllByIdIn(tableIds);

        validSameSize(tableIds, tables);
        validGroupable(tables);
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));

        final Long tableGroupId = savedTableGroup.getId();
        for (final Table savedTable : tables) {
            savedTable.putInGroup(tableGroupId);
            tableDao.save(savedTable);
        }
        return TableGroupResponse.of(savedTableGroup, tables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Table> tables = Optional.ofNullable(tableDao.findAllByTableGroupId(tableGroupId))
            .filter(list -> !list.isEmpty())
            .orElseThrow(IllegalArgumentException::new);

        final List<Long> orderTableIds = tables.stream()
            .map(Table::getId)
            .collect(Collectors.toList());

        if (isNotMealOver(orderTableIds)) {
            throw new IllegalArgumentException("식사가 끝나지 않은 테이블들의 그룹을 해제할 수 없습니다.");
        }
        for (final Table table : tables) {
            table.excludeFromGroup();
            tableDao.save(table);
        }
    }

    private boolean isNotMealOver(List<Long> orderTableIds) {
        return orderDao.existsByTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }

    private void validGroupable(List<Table> tables) {
        if (CollectionUtils.isEmpty(tables) || tables.size() < 2) {
            throw new IllegalArgumentException("그룹지을 테이블을 2개 이상 지정해주세요.");
        }
        for (final Table savedTable : tables) {
            if (!savedTable.isEmpty()) {
                throw new IllegalArgumentException("비어있지 않은 테이블을 새로운 그룹에 포함시킬 수 없습니다.");
            }
            if (savedTable.isGrouped()) {
                throw new IllegalArgumentException("이미 그룹이 지어진 테이블이 존재합니다.");
            }
        }
    }

    private void validSameSize(List<Long> tableIds, List<Table> savedTables) {
        if (tableIds.size() != savedTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블을 포함하여 그룹지을 수 없습니다.");
        }
    }
}
