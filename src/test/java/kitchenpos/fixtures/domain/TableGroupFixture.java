package kitchenpos.fixtures.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.OrderTableResponse;

public class TableGroupFixture {

    public static class TableGroupRequestBuilder {

        private final List<OrderTable> orderTables = new ArrayList<>();

        private LocalDateTime createdDate = LocalDateTime.now();

        public TableGroupRequestBuilder createdDate(final LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupRequestBuilder addOrderTables(final OrderTableRequest... orderTableRequests) {
            List<OrderTable> orderTables = Stream.of(orderTableRequests)
                    .map(OrderTableRequest::toEntity)
                    .collect(Collectors.toList());
            return addOrderTables(orderTables);
        }

        public TableGroupRequestBuilder addOrderTables(final OrderTableResponse... orderTableResponses) {
            List<OrderTable> orderTables = Stream.of(orderTableResponses)
                    .map(OrderTableResponse::toEntity)
                    .collect(Collectors.toList());
            return addOrderTables(orderTables);
        }

        public TableGroupRequestBuilder addOrderTables(final OrderTable... orderTables) {
            return addOrderTables(List.of(orderTables));
        }

        public TableGroupRequestBuilder addOrderTables(final List<OrderTable> orderTables) {
            this.orderTables.addAll(orderTables);
            return this;
        }

        public TableGroupRequest build() {
            List<Long> orderTableIds = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());

            return new TableGroupRequest(orderTableIds);
        }
    }
}
