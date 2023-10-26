package kitchenpos.common.domain;

import java.util.function.Function;

public class ValidResult {

    private static final ValidResult SUCCESS = new ValidResult(false, null);

    private final boolean isFailure;
    private final String errorMessage;

    public static ValidResult success() {
        return SUCCESS;
    }

    public static ValidResult failure(String errorMessage) {
        return new ValidResult(true, errorMessage);
    }

    private ValidResult(boolean isFailure, String errorMessage) {
        this.isFailure = isFailure;
        this.errorMessage = errorMessage;
    }

    public boolean isFailure() {
        return isFailure;
    }

    public <X extends Throwable> void throwIfFailure(Function<String, ? extends X> exceptionFunction) throws X {
        if (isFailure) {
            throw exceptionFunction.apply(errorMessage);
        }
    }
}
