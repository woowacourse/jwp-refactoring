package kitchenpos.application.dto;

public class OrderTableDto {

    Long id;

    public OrderTableDto() {
    }

    public OrderTableDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
