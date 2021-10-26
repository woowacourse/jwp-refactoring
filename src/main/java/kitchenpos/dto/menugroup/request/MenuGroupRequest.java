package kitchenpos.dto.menugroup.request;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroupRequest() {
        this(null);
    }

    public String getName() {
        return name;
    }
}
