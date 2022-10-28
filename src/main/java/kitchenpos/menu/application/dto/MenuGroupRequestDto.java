package kitchenpos.menu.application.dto;

public class MenuGroupRequestDto {

    private String name;

    public MenuGroupRequestDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
