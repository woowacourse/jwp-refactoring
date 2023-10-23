package kitchenpos.application.support.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static Long autoCount = 0L;

        private Long id = ++autoCount;
        private LocalDateTime createdDate = LocalDateTime.now();
        private List<OrderTable> orderTables = List.of(
                OrderTableTestSupport.builder().tableGroupId(id).build(),
                OrderTableTestSupport.builder().tableGroupId(id).build(),
                OrderTableTestSupport.builder().tableGroupId(id).build()
        );

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder createdDate(final LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder orderTables(final List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            final var result = new TableGroup();
            result.setId(id);
            result.setCreatedDate(createdDate);
            result.setOrderTables(orderTables);
            return result;
        }

        public TableGroupCreateRequest buildToTableGroupCreateRequest() {
            final List<OrderTableDto> orderTableDtos = orderTables.stream()
                    .map(it -> new OrderTableDto(it.getId()))
                    .collect(Collectors.toList());
            return new TableGroupCreateRequest(orderTableDtos);
        }
    }
}
