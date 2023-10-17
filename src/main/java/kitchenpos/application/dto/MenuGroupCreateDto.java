package kitchenpos.application.dto;

public class MenuGroupCreateDto {

    private final String name;

    public MenuGroupCreateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
