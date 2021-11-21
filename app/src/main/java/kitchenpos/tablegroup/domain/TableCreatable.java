package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.application.dto.TableGroupRequest;

public interface TableCreatable {
    void create(Long tableGroupId, TableGroupRequest request);
}
