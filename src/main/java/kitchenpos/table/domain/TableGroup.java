package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    private TableGroup(List<OrderTable> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables.addAll(orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public static class TableGroupBuilder {

        private List<OrderTable> orderTables;

        public TableGroupBuilder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(orderTables);
        }
    }
}
