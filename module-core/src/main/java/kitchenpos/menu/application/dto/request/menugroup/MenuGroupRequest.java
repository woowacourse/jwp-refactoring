package kitchenpos.menu.application.dto.request.menugroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
