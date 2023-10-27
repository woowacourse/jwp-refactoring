package kitchenpos.ordertable.application.dto;

public class OrderTableIdDto {

    private Long id;

    OrderTableIdDto() {

    }

    public OrderTableIdDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
