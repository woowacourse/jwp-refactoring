package kitchenpos.dto.request;

public class CreateMenuGroupRequest {

    private String name;

    public CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
