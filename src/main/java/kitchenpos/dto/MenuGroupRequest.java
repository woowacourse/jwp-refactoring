package kitchenpos.dto;

public class MenuGroupRequest {
    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
