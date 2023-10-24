package kitchenpos.domain;

import org.springframework.data.annotation.Id;

public class MenuGroup {

    @Id
    private final Long id;
    private final String name;

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupBuilder builder() {
        return new MenuGroupBuilder();
    }

    public static final class MenuGroupBuilder {
        private Long id;
        private String name;

        private MenuGroupBuilder() {
        }

        public MenuGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuGroupBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(id, name);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
