package kitchenpos.menugroup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class MenuGroupCreateRequest {

    private final String name;

    @JsonCreator
    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuGroupCreateRequest)) return false;
        MenuGroupCreateRequest request = (MenuGroupCreateRequest) o;
        return Objects.equals(name, request.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
