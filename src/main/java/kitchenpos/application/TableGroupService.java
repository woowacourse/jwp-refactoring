package kitchenpos.application;

import kitchenpos.application.mapper.TableGroupMapper;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupMapper tableGroupMapper;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableGroupMapper tableGroupMapper) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupMapper = tableGroupMapper;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = tableGroupMapper.from(request);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGroup.ungroup();
    }
}
