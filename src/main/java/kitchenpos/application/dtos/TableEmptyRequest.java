package kitchenpos.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TableEmptyRequest {
    private Boolean empty;

    public TableEmptyRequest(Boolean empty) {
        this.empty = empty;
    }
}
