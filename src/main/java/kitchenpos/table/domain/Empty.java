package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Empty {

    @NotNull
    private Boolean isEmpty;

    protected Empty() {
    }

    private Empty(Boolean isEmpty) {
        validate(isEmpty);

        this.isEmpty = isEmpty;
    }

    private void validate(Boolean empty) {
        if (empty == null) {
            throw new NullPointerException("empty는 null일 수 없습니다.");
        }
    }

    public static Empty from(Boolean empty) {
        return new Empty(empty);
    }

    public void change(Boolean empty) {
        this.isEmpty = empty;
    }

    public Boolean isEmpty() {
        return isEmpty;
    }

}
