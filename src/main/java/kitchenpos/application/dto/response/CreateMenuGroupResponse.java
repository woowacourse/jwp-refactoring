package kitchenpos.application.dto.response;

import kitchenpos.domain.menugroup.MenuGroup;

public class CreateMenuGroupResponse {
    private final Long id;
    private final String name;

    private CreateMenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CreateMenuGroupResponse from(MenuGroup menuGroup) {
        return builder()
                .id(menuGroup.getId())
                .name(menuGroup.getName())
                .build();
    }

    public static CreateMenuGroupResponseBuilder builder() {
        return new CreateMenuGroupResponseBuilder();
    }

    public static final class CreateMenuGroupResponseBuilder {
        private Long id;
        private String name;

        private CreateMenuGroupResponseBuilder() {
        }

        public CreateMenuGroupResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateMenuGroupResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateMenuGroupResponse build() {
            return new CreateMenuGroupResponse(id, name);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
