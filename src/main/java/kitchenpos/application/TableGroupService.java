package kitchenpos.application;

import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;

public interface TableGroupService {
    TableGroup create(TableGroupCreateRequest request);

    void unGroup(Long tableGroupId);
}
