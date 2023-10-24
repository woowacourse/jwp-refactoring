package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {
    private final Long id;
    private final String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return builder()
                .id(menuGroup.getId())
                .name(menuGroup.getName())
                .build();
    }

    public static MenuGroupResponseBuilder builder() {
        return new MenuGroupResponseBuilder();
    }

    public static final class MenuGroupResponseBuilder {
        private Long id;
        private String name;

        private MenuGroupResponseBuilder() {
        }

        public MenuGroupResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuGroupResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroupResponse build() {
            return new MenuGroupResponse(id, name);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
