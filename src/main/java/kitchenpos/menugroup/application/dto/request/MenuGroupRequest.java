package kitchenpos.menugroup.application.dto.request;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    protected MenuGroupRequest() {
    }

    public String getName() {
        return name;
    }
}
