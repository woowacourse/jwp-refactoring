package kitchenpos.menu.application.dto;

public class CreateMenuGroupDto {

    private String name;

    public CreateMenuGroupDto() {
    }

    public CreateMenuGroupDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
