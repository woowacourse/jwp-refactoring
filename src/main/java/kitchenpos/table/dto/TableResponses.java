package kitchenpos.table.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

import kitchenpos.table.domain.Table;

public class TableResponses {

    private final List<TableResponse> tableResponses;

    private TableResponses(List<TableResponse> tableResponses) {
        this.tableResponses = tableResponses;
    }

    public static TableResponses from(List<Table> tables) {
        return tables.stream()
            .map(TableResponse::from)
            .collect(collectingAndThen(toList(), TableResponses::new));
    }

    public List<TableResponse> getTableResponses() {
        return tableResponses;
    }
}
