package kitchenpos.application.dto.response;

public class MenuProductResponseDto {

    private Long seq;
    private Long quantity;

    private MenuProductResponseDto() {
    }

    public MenuProductResponseDto(Long seq, Long quantity) {
        this.seq = seq;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }
}
