package kitchenpos.ui.request;

import java.util.Objects;

public class MenuGroupRequest {
    private final String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuGroupRequest that = (MenuGroupRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "MenuGroupRequest{" +
                ", name='" + name + '\'' +
                '}';
    }
}
