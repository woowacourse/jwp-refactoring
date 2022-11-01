package kitchenpos.application.menu;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {
    }

    MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
