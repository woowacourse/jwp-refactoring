package kitchenpos.persistence.dto;

public class MenuGroupDataDto {

    private Long id;
    private String name;

    public MenuGroupDataDto(final Long id, final String name) {
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
