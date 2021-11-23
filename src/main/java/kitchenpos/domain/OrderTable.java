package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numberOfGuests;

    private Boolean empty;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "table_group_id")
//    private TableGroup tableGroup;

    protected OrderTable() {
    }

    public OrderTable(Long numberOfGuests, Boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(
        Long id,
        Long numberOfGuests,
        Boolean empty
    ) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
