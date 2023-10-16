package kitchenpos.application.dto;

public class MenuGroupCreateDto {

    private final String name;

    public MenuGroupCreateDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
