package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {
    
    private final String menuName;
    
    private final BigDecimal menuPrice;
    
    private final Long MenuGroupId;
    
    private final List<MenuProductCreateRequest> menuProductCreateRequests;
    
    public MenuCreateRequest(final String menuName,
                             final BigDecimal menuPrice,
                             final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProductCreateRequests) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        MenuGroupId = menuGroupId;
        this.menuProductCreateRequests = menuProductCreateRequests;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
    
    public Long getMenuGroupId() {
        return MenuGroupId;
    }
    
    public List<MenuProductCreateRequest> getMenuProductCreateRequests() {
        return menuProductCreateRequests;
    }
}
