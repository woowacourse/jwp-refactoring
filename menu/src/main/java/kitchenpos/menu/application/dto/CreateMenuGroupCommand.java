package kitchenpos.menu.application.dto;

public class CreateMenuGroupCommand {

    private final String name;

    public CreateMenuGroupCommand(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
