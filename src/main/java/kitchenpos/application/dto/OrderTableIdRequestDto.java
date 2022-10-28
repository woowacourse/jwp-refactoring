package kitchenpos.application.dto;

public class OrderTableIdRequestDto {

    private Long id;

    public OrderTableIdRequestDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
