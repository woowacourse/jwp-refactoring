package kitchenpos.menu.domain;

public class MenuChangedEvent {

    private Long originMenuId;
    private Menu menu;

    public MenuChangedEvent(Long originMenuId, Menu menu) {
        this.originMenuId = originMenuId;
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getOriginMenuId() {
        return originMenuId;
    }
}
