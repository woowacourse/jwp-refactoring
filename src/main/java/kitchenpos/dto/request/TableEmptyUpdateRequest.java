package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class TableEmptyUpdateRequest {

    @NotNull(message = "테이블의 빈 상태 정보를 입력해 주세요.")
    private final boolean empty;

    public TableEmptyUpdateRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
