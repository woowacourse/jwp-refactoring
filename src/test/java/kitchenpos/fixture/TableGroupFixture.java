package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;

public enum TableGroupFixture {

    TWO_TABLES(1L, null, List.of(OrderTableFixture.EMPTY_TABLE1.toDto(), OrderTableFixture.EMPTY_TABLE2.toDto())),
    ;

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableDto> orderTableDtos;

    TableGroupFixture(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTableDtos) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableDtos = orderTableDtos;
    }

    public static TableGroupDto computeDefaultTableGroupDto(Consumer<TableGroupDto> consumer) {
        TableGroupDto tableGroupDto = new TableGroupDto();
        tableGroupDto.setId(1L);
        tableGroupDto.setCreatedDate(null);
        tableGroupDto.setOrderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toDto(), OrderTableFixture.EMPTY_TABLE2.toDto()));
        consumer.accept(tableGroupDto);
        return tableGroupDto;
    }

    public TableGroupDto toDto() {
        TableGroupDto tableGroupDto = new TableGroupDto();
        tableGroupDto.setId(id);
        tableGroupDto.setCreatedDate(createdDate);
        tableGroupDto.setOrderTables(orderTableDtos);
        return tableGroupDto;
    }
}
