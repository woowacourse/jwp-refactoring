package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTableDtos;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTableDtos;
    }

    public void setOrderTables(final List<OrderTableDto> orderTableDtos) {
        this.orderTableDtos = orderTableDtos;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        TableGroupDto that = (TableGroupDto) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
