package kitchenpos.order.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class UngroupFailedException extends InvalidRequestException {


    public UngroupFailedException() {
        super("단체 지정을 해제할 수 없습니다.");
    }
}
