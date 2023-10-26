package kitchenpos.domain.menu;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuName {
    private static final int MAX_LENGTH = 255;

    @Column(nullable = false)
    private String name;

    public MenuName() {
    }

    public MenuName(final String name) {
        validation(name);
        this.name = name;
    }

    private void validation(final String name) {
        if (Objects.isNull(name) || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }
}
