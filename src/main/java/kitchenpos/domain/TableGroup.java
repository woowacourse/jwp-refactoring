package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public List<Long> extractOrderTableIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public void modifyOrderTables(final OrderTables orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables.getOrderTables();
    }

    public static class TableGroupBuilder {
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;

        public TableGroupBuilder() {
        }

        public TableGroupBuilder(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
            this.id = id;
            this.createdDate = createdDate;
            this.orderTables = orderTables;
        }

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupBuilder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(id, createdDate, orderTables);
        }
    }
}
