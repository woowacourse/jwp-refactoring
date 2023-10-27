package kitchenpos.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableIdRequest {

    private final Long id;

    public TableIdRequest(Long id) {
        this.id = id;
    }

    public static List<TableIdRequest> from(List<Long> ids) {
        return ids.stream()
                .map(TableIdRequest::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }
}
