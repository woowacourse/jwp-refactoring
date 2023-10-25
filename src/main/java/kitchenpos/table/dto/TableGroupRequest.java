package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupRequest {

    private List<OrderTableDto> orderTableDtos;

    @JsonCreator
    public TableGroupRequest(List<OrderTableDto> orderTableDtos) {
        this.orderTableDtos = orderTableDtos;
    }

    public List<OrderTableDto> getOrderTableDtos() {
        return orderTableDtos;
    }

    public static class OrderTableDto {

        private Long id;

        @JsonCreator
        public OrderTableDto(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
