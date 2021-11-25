package kitchenpos.menugroup.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import kitchenpos.menugroup.exception.InvalidMenuGroupNameException;

@Embeddable
public class MenuGroupName {

    @NotNull
    @Column(name = "name")
    private String value;

    protected MenuGroupName() {
    }

    public MenuGroupName(String value) {
        this.value = value;
        validateNull(this.value);
        validateBlank(this.value);
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new InvalidMenuGroupNameException("이름은 Null일 수 없습니다.");
        }
    }

    private void validateBlank(String value) {
        if (value.replaceAll(" ", "").isEmpty()) {
            throw new InvalidMenuGroupNameException("이름은 공백으로 이루어질 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuGroupName menuGroupName = (MenuGroupName) o;
        return Objects.equals(value, menuGroupName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
