package kitchenpos.ordertable.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupDto {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    public static TableGroupDto from(TableGroup entity) {
        List<OrderTableDto> orderTableDtos = entity.getOrderTables()
                                                   .stream()
                                                   .map(OrderTableDto::from)
                                                   .collect(toList());
        TableGroupDto tableGroupDto = new TableGroupDto();
        tableGroupDto.setId(entity.getId());
        tableGroupDto.setCreatedDate(entity.getCreatedDate());
        tableGroupDto.setOrderTables(orderTableDtos);
        return tableGroupDto;
    }

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
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
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
