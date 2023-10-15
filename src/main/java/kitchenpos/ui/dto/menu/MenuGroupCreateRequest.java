package kitchenpos.ui.dto.menu;

public class MenuGroupCreateRequest {

    private String name;

    MenuGroupCreateRequest() {

    }

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
