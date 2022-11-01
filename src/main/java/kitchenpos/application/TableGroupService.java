package kitchenpos.application;

import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;

public interface TableGroupService {
    TableGroupResponse create(TableGroupCreateRequest request);

    void unGroup(Long tableGroupId);
}
