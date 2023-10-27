package kitchenpos.menugroup.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menugroup.domain.exception.MenuGroupException.InvalidMenuGroupNameException;
import org.springframework.lang.NonNull;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NonNull
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(final String name) {
        this.name = name;
    }

    public static MenuGroup from(final String name) {
        validateName(name);
        return new MenuGroup(name);
    }

    private static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidMenuGroupNameException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
