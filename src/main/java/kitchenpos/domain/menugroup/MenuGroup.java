package kitchenpos.domain.menugroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Embedded
    private MenuGroupName name;

    public MenuGroup() {
    }

    public MenuGroup(final MenuGroupName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}
