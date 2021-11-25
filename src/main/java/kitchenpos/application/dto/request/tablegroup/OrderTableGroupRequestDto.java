package kitchenpos.application.dto.request.tablegroup;

public class OrderTableGroupRequestDto {

    private Long id;

    private OrderTableGroupRequestDto() {
    }

    public OrderTableGroupRequestDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
