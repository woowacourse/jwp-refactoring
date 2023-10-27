package kitchenpos.menu.dto.request;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroupCreateRequest() {
    }

    public String getName() {
        return name;
    }

}
