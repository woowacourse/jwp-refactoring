package kitchenpos.application.dto;

public class CreateMenuGroupDto {

    private String name;

    public CreateMenuGroupDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
