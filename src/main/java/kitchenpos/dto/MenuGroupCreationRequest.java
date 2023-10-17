package kitchenpos.dto;

public class MenuGroupCreationRequest {

    private final String name;

    public MenuGroupCreationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
