package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.exception.MenuGroupException.NoMenuGroupNameException;
import org.springframework.util.StringUtils;

@Entity
public class MenuGroup {

    public static int MINIMUM_MENU_GROUP_NAME_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    public MenuGroup(final Long id, final String name) {
        validate(name);
        this.id = id;
        this.name = name;
    }

    private void validate(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new NoMenuGroupNameException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
