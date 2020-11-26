package kitchenpos.domain.menugroup;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupName {

    @Column(nullable = false)
    private String name;

    protected MenuGroupName() {
    }

    protected MenuGroupName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("MenuGroup name은 null일 수 없습니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("MenuGroup name은 비어있을 수 없습니다.");
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
        MenuGroupName that = (MenuGroupName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "MenuGroupName{" +
            "name='" + name + '\'' +
            '}';
    }
}
