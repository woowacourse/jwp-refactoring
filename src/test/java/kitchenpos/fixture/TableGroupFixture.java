package kitchenpos.fixture;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;

public enum TableGroupFixture {

    TWO_TABLES(1L, null, List.of(OrderTableFixture.OCCUPIED_TABLE_OF_GROUP1,
        OrderTableFixture.EMPTY_TABLE2)),
    TABLE_GROUP_AVAILABLE(2L, null, List.of(OrderTableFixture.EMPTY_TABLE1,
        OrderTableFixture.EMPTY_TABLE2)),
    ;

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableFixture> orderTableFixtures;

    TableGroupFixture(Long id, LocalDateTime createdDate,
        List<OrderTableFixture> orderTableFixtures) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableFixtures = orderTableFixtures;
    }

    public static TableGroupDto computeDefaultTableGroupDto(Consumer<TableGroupDto> consumer) {
        TableGroupDto tableGroupDto = new TableGroupDto();
        tableGroupDto.setId(1L);
        tableGroupDto.setCreatedDate(null);
        tableGroupDto.setOrderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toDto(),
            OrderTableFixture.EMPTY_TABLE2.toDto()));
        consumer.accept(tableGroupDto);
        return tableGroupDto;
    }

    public TableGroupDto toDto() {
        List<OrderTableDto> orderTableDtos = orderTableFixtures.stream()
                                                               .map(OrderTableFixture::toDto)
                                                               .collect(toList());

        TableGroupDto tableGroupDto = new TableGroupDto();
        tableGroupDto.setId(id);
        tableGroupDto.setCreatedDate(createdDate);
        tableGroupDto.setOrderTables(orderTableDtos);
        return tableGroupDto;
    }

    public TableGroup toEntity() {
        List<OrderTable> orderTables = orderTableFixtures.stream()
                                                         .map(OrderTableFixture::toEntity)
                                                         .collect(toList());
        return new TableGroup.Builder()
            .orderTables(orderTables)
            .build();
    }
}
