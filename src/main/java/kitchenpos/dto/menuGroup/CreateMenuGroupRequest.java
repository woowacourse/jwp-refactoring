package kitchenpos.dto.menuGroup;

public class CreateMenuGroupRequest {

    private String name;

    public CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
