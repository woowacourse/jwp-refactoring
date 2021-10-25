package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    private MenuGroup(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;

        private Builder() {
        }

        public Builder of(MenuGroup menuGroup) {
            this.id = menuGroup.id;
            this.name = menuGroup.name;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;

        }

        public MenuGroup build() {
            return new MenuGroup(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
