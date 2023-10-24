package kitchenpos.application.dto.request;

public class MenuGroupCreateRequest {

    private final String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
