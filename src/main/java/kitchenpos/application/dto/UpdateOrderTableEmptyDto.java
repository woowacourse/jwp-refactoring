package kitchenpos.application.dto;

public class UpdateOrderTableEmptyDto {

    private Long id;
    private Boolean empty;

    public UpdateOrderTableEmptyDto(Long id, Boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
