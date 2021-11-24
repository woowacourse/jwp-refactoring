package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.ui.dto.TableAssembler;
import kitchenpos.ui.dto.request.TableEmptyRequest;
import kitchenpos.ui.dto.request.TableNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.TableRequest;
import kitchenpos.ui.dto.response.TableEmptyResponse;
import kitchenpos.ui.dto.response.TableNumberOfGuestsResponse;
import kitchenpos.ui.dto.response.TableResponse;
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
public class TableController {

    private final TableService tableService;

    public TableController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<TableResponse> create(@RequestBody TableRequest request) {
        TableResponse response = TableAssembler.tableResponse(
            tableService.create(TableAssembler.tableRequestDto(request)));
        URI uri = URI.create("/api/tables/" + response.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> list() {
        List<TableResponse> responses = TableAssembler
            .tableResponses(tableService.list());

        return ResponseEntity.ok().body(responses);
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<TableEmptyResponse> changeEmpty(
        @PathVariable Long orderTableId,
        @RequestBody TableEmptyRequest request
    ) {
        TableEmptyResponse response = TableAssembler
            .tableEmptyResponse(tableService.changeEmpty(
                TableAssembler.tableEmptyRequestDto(orderTableId, request)
            ));

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<TableNumberOfGuestsResponse> changeNumberOfGuests(
        @PathVariable Long orderTableId,
        @RequestBody TableNumberOfGuestsRequest request
    ) {
        TableNumberOfGuestsResponse response = TableAssembler
            .tableNumberOfGuestsResponse(tableService.changeNumberOfGuests(
                TableAssembler.tableNumberOfGuestsRequestDto(orderTableId, request)
            ));

        return ResponseEntity.ok().body(response);
    }
}
