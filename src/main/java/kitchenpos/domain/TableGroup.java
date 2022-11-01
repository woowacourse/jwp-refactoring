package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.application.event.TableGroupEvent;
import kitchenpos.application.event.TableUnGroupEvent;
import kitchenpos.domain.validator.TableGroupValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @CreatedDate
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    TableGroup(final List<Long> orderTableIds) {
        registerEvent(new TableGroupEvent(this, new ArrayList<>(orderTableIds)));
    }

    public TableGroup unGroup(final TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateUnGroup(this);

        registerEvent(new TableUnGroupEvent(this.id));

        return this;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
