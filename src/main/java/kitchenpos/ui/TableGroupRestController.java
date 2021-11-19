package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.table.OrderTableDto;
import kitchenpos.ui.dto.request.table.TableGroupRequestDto;
import kitchenpos.ui.dto.response.table.OrderTableResponseDto;
import kitchenpos.ui.dto.response.table.TableGroupResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupRestController {
    private final TableGroupService tableGroupService;

    public TableGroupRestController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponseDto> create(
        @RequestBody final TableGroupRequestDto tableGroupRequestDto
    ) {
        final TableGroup created = tableGroupService.create(toTableGroup(tableGroupRequestDto));
        final TableGroupResponseDto responseDto = new TableGroupResponseDto(
            created.getId(),
            created.getCreatedDate(),
            toOrderTableResponseDto(created.getOrderTables())
        );

        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    private List<OrderTableResponseDto> toOrderTableResponseDto(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable ->
                new OrderTableResponseDto(
                    orderTable.getId(),
                    orderTable.getTableGroupId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty()
                )).collect(toList());
    }

    private TableGroup toTableGroup(TableGroupRequestDto tableGroupRequestDto) {
        return new TableGroup(toOrderTables(tableGroupRequestDto.getOrderTables()));
    }

    private List<OrderTable> toOrderTables(List<OrderTableDto> orderTables) {
        return orderTables.stream()
            .map(orderTableDto -> new OrderTable(orderTableDto.getId()))
            .collect(toList());
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent()
            .build()
            ;
    }
}
