package kitchenpos.domain.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class Name {
    @NotNull
    private String value;

    protected Name() {
    }

    private Name(final String value) {
        validateName(value);
        this.value = value;
    }

    private static void validateName(final String value) {
        if (value == null || value.isEmpty() || value.isBlank()) {
            throw new IllegalArgumentException("이름이 입력되지 않았습니다.");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("이름은 255자를 초과할 수 없습니다.");
        }
        if (!value.matches("^[가-힣a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("이름은 한글, 영문, 숫자만 입력 가능합니다.");
        }
        if (value.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("이름은 숫자만 입력할 수 없습니다.");
        }
    }

    public static Name of(final String name) {
        return new Name(name);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String getValue() {
        return value;
    }
}
