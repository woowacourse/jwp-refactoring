package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import kitchenpos.exception.InvalidMenuGroupException;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
        validateName(this.name);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.replaceAll(" ", "").isEmpty()) {
            throw new InvalidMenuGroupException("MenuGroup 이름은 Null이거나 공백일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
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
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
