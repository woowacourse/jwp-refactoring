package kitchenpos.menugroup.dto;

public class CreateMenuGroupDto {

    private final String name;

    public CreateMenuGroupDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
