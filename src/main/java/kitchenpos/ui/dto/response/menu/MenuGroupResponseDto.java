package kitchenpos.ui.dto.response.menu;

public class MenuGroupResponseDto {

    private Long id;
    private String name;

    private MenuGroupResponseDto() {
    }

    public MenuGroupResponseDto(Long id, String name) {
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
