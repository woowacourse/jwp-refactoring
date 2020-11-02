package kitchenpos.domain.menu;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuName {

    @Column(nullable = false)
    private String name;

    protected MenuName() {
    }

    protected MenuName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new NullPointerException("Menu Name은 Null일 수 없습니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Menu Name은 비어있을 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuName menuName = (MenuName) o;
        return Objects.equals(name, menuName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "MenuName{" +
            "name='" + name + '\'' +
            '}';
    }
}
