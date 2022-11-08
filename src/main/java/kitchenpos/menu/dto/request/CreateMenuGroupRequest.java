package kitchenpos.menu.dto.request;

public class CreateMenuGroupRequest {

    private String name;

    private CreateMenuGroupRequest() {
    }

    public CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
