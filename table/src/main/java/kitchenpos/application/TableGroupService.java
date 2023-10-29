package kitchenpos.application;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableMapper tableMapper;
    private final TableValidator tableValidator;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            TableMapper tableMapper,
            TableValidator tableValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableMapper = tableMapper;
        this.tableValidator = tableValidator;
    }

    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                tableGroupCreateRequest.getTableIds(),
                tableMapper,
                tableValidator
        );
        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = getTableGroup(tableGroupId);
        tableGroup.ungroup(tableValidator);
    }

    private TableGroup getTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블 그룹입니다."));
    }
}
