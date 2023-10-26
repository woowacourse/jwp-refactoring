package kitchenpos.dto.request;

public class CreateMenuGroupRequest {
    private String name;

    private CreateMenuGroupRequest() {
    }

    private CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public static CreateMenuGroupRequestBuilder builder() {
        return new CreateMenuGroupRequestBuilder();
    }

    public static final class CreateMenuGroupRequestBuilder {
        private String name;

        private CreateMenuGroupRequestBuilder() {
        }

        public CreateMenuGroupRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateMenuGroupRequest build() {
            return new CreateMenuGroupRequest(name);
        }
    }

    public String getName() {
        return name;
    }
}
