package kitchenpos.newdomain;

import kitchenpos.newdomain.vo.Name;

public class MenuGroup {

    private Long id;
    private Name name;

    public MenuGroup(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }
}
