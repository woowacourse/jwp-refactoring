package kitchenpos.ordertable.service;

import java.util.Objects;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;

public class OrderTableDto {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTableDto from(OrderTable entity) {
        OrderTableDto orderTableDto = new OrderTableDto();
        orderTableDto.setId(entity.getId());
        TableGroup tableGroup = entity.getTableGroup();
        if (tableGroup == null) {
            orderTableDto.setTableGroupId(null);
        } else {
            orderTableDto.setTableGroupId(tableGroup.getId());
        }
        orderTableDto.setNumberOfGuests(entity.getNumberOfGuests());
        orderTableDto.setEmpty(entity.isEmpty());
        return orderTableDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        OrderTableDto table = (OrderTableDto) object;
        return Objects.equals(id, table.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
