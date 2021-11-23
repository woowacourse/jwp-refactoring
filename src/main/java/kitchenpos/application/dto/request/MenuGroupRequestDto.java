package kitchenpos.application.dto.request;

public class MenuGroupRequestDto {

    private String name;

    public MenuGroupRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
