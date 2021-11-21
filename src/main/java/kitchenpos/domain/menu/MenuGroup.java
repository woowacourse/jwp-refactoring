package kitchenpos.domain.menu;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    public MenuGroup() {
    }

    private MenuGroup(MenuGroupBuilder menuGroupBuilder) {
        this.id = menuGroupBuilder.id;
        this.name = menuGroupBuilder.name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
        return Objects.equals(id, menuGroup.id) && Objects
                .equals(name, menuGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static class MenuGroupBuilder {

        private Long id;
        private String name;

        public MenuGroupBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public MenuGroupBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(this);
        }
    }
}
