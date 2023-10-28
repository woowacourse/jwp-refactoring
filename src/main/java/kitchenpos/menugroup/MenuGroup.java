package kitchenpos.menugroup;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder {

        private Long id;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(id, name);
        }
    }
}
