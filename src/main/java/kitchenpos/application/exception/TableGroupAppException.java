package kitchenpos.application.exception;

import kitchenpos.common.KitchenPosException;

public class TableGroupAppException extends KitchenPosException {

    private static final String NOT_FOUND_TABLE_GROUP_MESSAGE = "테이블 그룹을 찾을 수 없습니다. id = ";

    public TableGroupAppException(final String message) {
        super(message);
    }

    public static class NotFoundTableGroupException extends TableGroupAppException {

        public NotFoundTableGroupException(final Long tableGroupId) {
            super(NOT_FOUND_TABLE_GROUP_MESSAGE + tableGroupId);
        }
    }
}
