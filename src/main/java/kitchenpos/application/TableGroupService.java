package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.CreateTableGroupCommand;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.application.verifier.CreateTableGroupVerifier;
import kitchenpos.application.verifier.UngroupTableVerifier;
import kitchenpos.domain.model.tablegroup.TableGroup;
import kitchenpos.domain.model.tablegroup.TableGroupRepository;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final CreateTableGroupVerifier createTableGroupVerifier;
    private final UngroupTableVerifier ungroupTableVerifier;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
            CreateTableGroupVerifier createTableGroupVerifier,
            UngroupTableVerifier ungroupTableVerifier) {
        this.tableGroupRepository = tableGroupRepository;
        this.createTableGroupVerifier = createTableGroupVerifier;
        this.ungroupTableVerifier = ungroupTableVerifier;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupCommand command) {
        TableGroup tableGroup = createTableGroupVerifier.toTableGroup(command.getOrderTables());
        TableGroup saved = tableGroupRepository.save(tableGroup.create());
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long id) {
        TableGroup tableGroup = tableGroupRepository.findByIdWithOrderTables(id)
                .orElseThrow(IllegalArgumentException::new);
        ungroupTableVerifier.verify(tableGroup.orderTableIds());
        tableGroupRepository.deleteById(id);
    }
}
