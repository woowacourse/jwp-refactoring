package kitchenpos.domain.exception;

import kitchenpos.basic.BasicException;

public class TableGroupException extends BasicException {

    public TableGroupException(final TableGroupExceptionType exceptionType) {
        super(exceptionType);
    }
}
