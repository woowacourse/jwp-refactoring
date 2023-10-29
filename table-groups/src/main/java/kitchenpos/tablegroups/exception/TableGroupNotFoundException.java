package kitchenpos.tablegroups.exception;

import kitchenpos.common.exception.NotFoundException;

public class TableGroupNotFoundException extends NotFoundException {
    private static final String RESOURCE = "테이블 그룹";

    public TableGroupNotFoundException(final Long tableGroupId) {
        super(RESOURCE, tableGroupId);
    }
}
