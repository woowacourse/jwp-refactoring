package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableCreatable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDeletable;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableCreatable tableCreator;
    private final TableGroupDeletable tableEraser;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final TableCreatable tableCreator,
                             final TableGroupDeletable tableEraser) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableCreator = tableCreator;
        this.tableEraser = tableEraser;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup());
        tableCreator.create(savedTableGroup.getId(), request);

        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableEraser.ungroup(tableGroupId);
    }

    private TableGroup createTableGroup() {
        return TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .build();
    }
}
