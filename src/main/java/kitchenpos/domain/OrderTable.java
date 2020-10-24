package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();
    @Version
    private int version;

    @Builder
    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void groupBy(final TableGroup tableGroup) {
        validateEmpty();
        validateTableGroup();
        if (Objects.isNull(this.tableGroup)) {
            changeEmpty(false);
            this.tableGroup = tableGroup;
            this.tableGroup.addOrderTable(this);
        } else if (Objects.isNull(tableGroup)) {
            ungroup();
        }
    }

    private void validateEmpty() {
        if (!empty) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        this.tableGroup.removeOrderTable(this);
        this.tableGroup = null;
        changeEmpty(false);
    }

    public void changeEmpty(final boolean empty) {
        validateTableGroup();
        this.empty = empty;
    }

    private void validateTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrder(final Order order) {
        orders.add(order);
    }

    public boolean isEmpty() {
        return empty;
    }
}
