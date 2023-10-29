package kitchenpos.application;

import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.CreateTableGroupRequest;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest request) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        savedTableGroup.group(request.getOrderTableIds());

        return tableGroupRepository.save(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup =
                tableGroupRepository.findById(tableGroupId)
                                    .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));

        tableGroup.ungroup();

        tableGroupRepository.save(tableGroup);
    }
}
