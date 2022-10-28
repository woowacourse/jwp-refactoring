package kitchenpos.application.dto.request;

public class MenuGroupCreateRequest {
    private String name;

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
