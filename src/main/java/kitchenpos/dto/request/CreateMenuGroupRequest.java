package kitchenpos.dto.request;

public class CreateMenuGroupRequest {

    private String name;

    public CreateMenuGroupRequest() {
    }

    public CreateMenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
