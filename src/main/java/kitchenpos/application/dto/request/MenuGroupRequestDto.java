package kitchenpos.application.dto.request;

public class MenuGroupRequestDto {

    private String name;

    private MenuGroupRequestDto() {
    }

    public MenuGroupRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
