package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import kitchenpos.exception.InvalidNameException;

@Embeddable
public class Name {

    @NotNull
    @Column(name = "name")
    private String value;

    protected Name() {
    }

    public Name(String value) {
        this.value = value;
        validateNull(this.value);
        validateBlank(this.value);
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new InvalidNameException("이름은 Null일 수 없습니다.");
        }
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            throw new InvalidNameException("이름은 공백으로 이루어질 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
