package kitchenpos.menugroup.presentation.dto;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
