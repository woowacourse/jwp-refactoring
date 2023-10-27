package kitchenpos.menu.dto;

public class ValidateExistMenuDto {

    private Long menuId;

    public ValidateExistMenuDto(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }
}
