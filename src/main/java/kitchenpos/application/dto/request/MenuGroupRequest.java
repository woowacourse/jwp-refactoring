package kitchenpos.application.dto.request;

public class MenuGroupRequest {

    private final String name;

    private MenuGroupRequest() {
        this(null);
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
