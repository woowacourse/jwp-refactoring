package kitchenpos.menu.ui.request;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    protected MenuGroupCreateRequest() {
    }

    public String getName() {
        return name;
    }
}
