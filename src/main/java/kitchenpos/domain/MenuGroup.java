package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    private MenuGroup() {
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
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.id = id;
            menuGroup.name = name;
            return menuGroup;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
