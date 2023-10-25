package kitchenpos.ui.request;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public MenuGroupCreateRequest() {
    }

    public String getName() {
        return name;
    }
}
