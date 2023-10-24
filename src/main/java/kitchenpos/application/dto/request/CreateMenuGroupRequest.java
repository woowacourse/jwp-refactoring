package kitchenpos.application.dto.request;

public class CreateMenuGroupRequest {
    private Long id;
    private String name;

    private CreateMenuGroupRequest() {
    }

    private CreateMenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CreateMenuGroupRequestBuilder builder() {
        return new CreateMenuGroupRequestBuilder();
    }

    public static final class CreateMenuGroupRequestBuilder {
        private Long id;
        private String name;

        private CreateMenuGroupRequestBuilder() {
        }

        public CreateMenuGroupRequestBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateMenuGroupRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateMenuGroupRequest build() {
            return new CreateMenuGroupRequest(id, name);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
