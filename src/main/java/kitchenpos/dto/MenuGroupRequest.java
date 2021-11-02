package kitchenpos.dto;

public class MenuGroupRequest {

    private Long id;
    private String name;

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
