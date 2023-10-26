package kitchenpos.menu.dto;

public class MenuUpdateRequest {

    private String name;
    private Long price;

    private MenuUpdateRequest() {
    }

    public MenuUpdateRequest(String name, Long price) {
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
