package kitchenpos.application.dto.request;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
