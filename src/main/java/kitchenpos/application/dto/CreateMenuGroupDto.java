package kitchenpos.application.dto;

public class CreateMenuGroupDto {

    private final String name;

    public CreateMenuGroupDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
