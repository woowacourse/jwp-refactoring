package kitchenpos.domain;

public class MenuGroup {

    private Long id;
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
