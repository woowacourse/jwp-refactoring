package kitchenpos.tablegroup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGrouping tableGrouper;
    private final JpaTableGroupRepository jpaTableGroupRepository;

    public TableGroupService(
            TableGrouping tableGrouper,
            JpaTableGroupRepository jpaTableGroupRepository
    ) {
        this.tableGrouper = tableGrouper;
        this.jpaTableGroupRepository = jpaTableGroupRepository;
    }

    @Transactional
    public Long create(final TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = jpaTableGroupRepository.save(tableGroup);
        tableGrouper.group(savedTableGroup, orderTableIds);

        return savedTableGroup.getId();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = jpaTableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGrouper.ungroup(tableGroup);
    }
}
