package kitchenpos.application.dto.menugroup;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {}

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}