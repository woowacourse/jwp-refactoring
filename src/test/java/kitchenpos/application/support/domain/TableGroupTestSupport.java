package kitchenpos.application.support.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private LocalDateTime createdDate = LocalDateTime.now();
        private List<OrderTable> orderTables = List.of(
                OrderTableTestSupport.builder().build(),
                OrderTableTestSupport.builder().build(),
                OrderTableTestSupport.builder().build()
        );

        public Builder createdDate(final LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder orderTables(final List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(createdDate);
        }

        public TableGroupCreateRequest buildToTableGroupCreateRequest() {
            final List<OrderTableRequest> orderTableRequests = orderTables.stream()
                    .map(it -> new OrderTableRequest(it.getId()))
                    .collect(Collectors.toList());
            return new TableGroupCreateRequest(orderTableRequests);
        }
    }
}
