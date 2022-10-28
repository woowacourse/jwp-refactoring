package kitchenpos.application.dto;

public class MenuGroupResponseDto {

    private final Long id;
    private final String name;

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
