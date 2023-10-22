package kitchenpos.menu;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuName {
    private String name;

    public MenuName(final String name) {
        validate(name);
        this.name = name;
    }

    protected MenuName() {
    }

    private void validate(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("메뉴 이름은 공백이 될 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuName menuName = (MenuName) o;
        return Objects.equals(name, menuName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
