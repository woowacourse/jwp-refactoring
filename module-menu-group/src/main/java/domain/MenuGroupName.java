package domain;

import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupName {

    private final String name;

    public MenuGroupName() {
        name = null;
    }

    public MenuGroupName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
