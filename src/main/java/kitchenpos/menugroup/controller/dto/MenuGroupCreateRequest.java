package kitchenpos.menugroup.controller.dto;

public class MenuGroupCreateRequest {
    
    private final String name;
    
    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
