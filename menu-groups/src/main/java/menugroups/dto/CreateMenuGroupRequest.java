package menugroups.dto;

public class CreateMenuGroupRequest {
    private String name;

    private CreateMenuGroupRequest(final String name) {
        this.name = name;
    }

    public CreateMenuGroupRequest() {
    }

    public static CreateMenuGroupRequest of(final String name) {
        return new CreateMenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }
}
