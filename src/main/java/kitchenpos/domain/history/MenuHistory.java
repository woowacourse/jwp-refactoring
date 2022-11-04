package kitchenpos.domain.history;

import java.time.LocalDateTime;

public class MenuHistory implements Comparable<MenuHistory> {

    private final Long id;

    private final Long menuId;

    private final Long priceAtTime;

    private final String nameAtTime;

    private final LocalDateTime createDate;

    public MenuHistory(Long id, Long menuId, Long priceAtTime, String nameAtTime, LocalDateTime createDate) {
        this.id = id;
        this.menuId = menuId;
        this.priceAtTime = priceAtTime;
        this.nameAtTime = nameAtTime;
        this.createDate = createDate;
    }

    public MenuHistory(Long menuId, Long priceAtTime, String nameAtTime, LocalDateTime createDate) {
        this(null, menuId, priceAtTime, nameAtTime, createDate);
    }

    @Override
    public int compareTo(MenuHistory o) {
        return this.getCreateDate().compareTo(o.getCreateDate());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getPriceAtTime() {
        return priceAtTime;
    }

    public String getNameAtTime() {
        return nameAtTime;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
