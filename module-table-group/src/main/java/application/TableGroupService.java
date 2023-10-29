package application;

import application.dto.TableGroupCreateRequest;
import application.dto.TableGroupResponse;
import domain.TableGroup;
import domain.TableGroupValidator;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTables = request.getOrderTables();
        final LocalDateTime createdTime = LocalDateTime.now();
        final TableGroup tableGroup = new TableGroup(orderTables, createdTime, tableGroupValidator);

        return TableGroupResponse.from(
                tableGroupRepository.save(tableGroup),
                orderTables
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }
}
