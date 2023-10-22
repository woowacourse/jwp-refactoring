package kitchenpos.domain.menu;

import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.exception.MenuException.NoMenuNameException;

@Embeddable
public class MenuName {

    private final String name;

    public MenuName() {
        name = null;
    }

    public MenuName(final String name) {
        validateNoName(name);
        this.name = name;
    }

    private void validateNoName(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new NoMenuNameException();
        }
    }

    public String getName() {
        return name;
    }
}
