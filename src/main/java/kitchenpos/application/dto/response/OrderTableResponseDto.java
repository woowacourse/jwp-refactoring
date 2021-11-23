package kitchenpos.application.dto.response;

public class OrderTableResponseDto {

    private Long id;

    public OrderTableResponseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
