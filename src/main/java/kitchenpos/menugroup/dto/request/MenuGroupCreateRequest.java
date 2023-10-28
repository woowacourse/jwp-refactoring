package kitchenpos.menugroup.dto.request;

public class MenuGroupCreateRequest {

    private final String menuGroupName;

    public MenuGroupCreateRequest(String menuGroupName) {
        this.menuGroupName = menuGroupName;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }
}
