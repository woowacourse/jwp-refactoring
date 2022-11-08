package kitchenpos.dto;

import java.time.LocalDateTime;

public class MenuNameChangeRequest {

    private Long menuId;
    private String newName;
    private LocalDateTime updateTime;

    public MenuNameChangeRequest(Long menuId, String newName, LocalDateTime updateTime) {
        this.menuId = menuId;
        this.newName = newName;
        this.updateTime = updateTime;
    }

    public MenuNameChangeRequest() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getNewName() {
        return newName;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
}
