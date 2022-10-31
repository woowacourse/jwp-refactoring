package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.dto.TableDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tables")
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<TableDto> create(@RequestBody TableDto tableDto) {
        TableDto created = tableService.create(tableDto);
        URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping
    public ResponseEntity<List<TableDto>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<TableDto> changeEmpty(@PathVariable Long orderTableId, @RequestBody TableDto tableDto) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId, tableDto))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<TableDto> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                         @RequestBody TableDto tableDto) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, tableDto))
                ;
    }
}
