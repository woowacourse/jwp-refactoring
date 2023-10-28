package kitchenpos.domain.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class Name {
    public static final String NAME_IS_NOT_PROVIDED_ERROR_MESSAGE = "이름이 입력되지 않았습니다.";
    public static final String NAME_CANNOT_EXCEED_255_ERROR_MESSAGE = "이름은 255자를 초과할 수 없습니다.";
    public static final String NAME_CANNOT_CONTAIN_ONLY_NUMBER_ERROR_MESSAGE = "이름은 숫자만 입력할 수 없습니다.";
    
    @NotNull
    private String name;

    protected Name() {
    }

    private Name(final String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(final String value) {
        if (value == null || value.isEmpty() || value.isBlank()) {
            throw new IllegalArgumentException(NAME_IS_NOT_PROVIDED_ERROR_MESSAGE);
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException(NAME_CANNOT_EXCEED_255_ERROR_MESSAGE);
        }
        if (value.matches("^[0-9]+$")) {
            throw new IllegalArgumentException(NAME_CANNOT_CONTAIN_ONLY_NUMBER_ERROR_MESSAGE);
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
        return Objects.equals(this.name, name.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
