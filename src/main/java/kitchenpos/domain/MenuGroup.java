package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.badrequest.MenuGroupNameInvalidException;
import org.springframework.util.StringUtils;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;

        validateName();
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    private void validateName() {
        if (!StringUtils.hasText(this.name)) {
            throw new MenuGroupNameInvalidException(name);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
