package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class OrderTableEmptyRequest {

    @NotNull(message = "테이블 상태는 null일 수 없습니다.")
    private boolean empty;

    protected OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
