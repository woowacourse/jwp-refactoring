package kitchenpos.dto;

import java.time.LocalDateTime;

public class MenuPriceChangeRequest {

    private Long menuId;
    private Long newPrice;
    private LocalDateTime updateTime;

    public MenuPriceChangeRequest(Long menuId, Long newPrice, LocalDateTime updateTime) {
        this.menuId = menuId;
        this.newPrice = newPrice;
        this.updateTime = updateTime;
    }

    public MenuPriceChangeRequest() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getNewPrice() {
        return newPrice;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
}
