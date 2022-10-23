package kitchenpos.application.dto;

import lombok.Getter;

@Getter
public class EmptyTableDto {

    private final Long orderTableId;
    private final Boolean empty;

    public EmptyTableDto(Long orderTableId, Boolean empty) {
        this.orderTableId = orderTableId;
        this.empty = empty;
    }
}
