package menu.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuGroupCreateRequest {
    private final String name;

    @JsonCreator
    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
