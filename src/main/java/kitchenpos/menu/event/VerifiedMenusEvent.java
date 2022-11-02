package kitchenpos.menu.event;

import java.util.List;

public class VerifiedMenusEvent {

    private List<Long> menuIds;

    public VerifiedMenusEvent(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

}
