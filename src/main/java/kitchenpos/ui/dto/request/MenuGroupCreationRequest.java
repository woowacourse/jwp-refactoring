package kitchenpos.ui.dto.request;

public class MenuGroupCreationRequest {

    private String name;

    private MenuGroupCreationRequest() {}

    public MenuGroupCreationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
