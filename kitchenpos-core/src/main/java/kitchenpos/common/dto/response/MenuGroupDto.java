package kitchenpos.common.dto.response;

public class MenuGroupDto {

    private Long id;
    private String name;

    public MenuGroupDto() {
    }

    public MenuGroupDto(final Long id, final String name) {
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
