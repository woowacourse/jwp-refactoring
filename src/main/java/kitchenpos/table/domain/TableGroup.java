package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.table.exception.IllegalTableStateException;
import kitchenpos.table.exception.InvalidTableQuantityException;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<Table> tables;

    public TableGroup() {
    }

    public TableGroup(List<Table> tables) {
        this(null, tables);
    }

    public TableGroup(Long id, List<Table> tables) {
        validate(tables);
        this.id = id;
        this.tables = tables;
    }

    private void validate(List<Table> tables) {
        if (tables.size() < 2) {
            throw new InvalidTableQuantityException();
        }

        for (final Table savedTable : tables) {
            if (savedTable.existCustomer() || savedTable.hasGroup()) {
                throw new IllegalTableStateException();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Table> getTables() {
        return tables;
    }
}
