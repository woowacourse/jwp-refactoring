package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderTable {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    
    private int numberOfGuests;
    
    private boolean empty;
    
    public OrderTable(final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }
    
    public OrderTable(final Long id,
                      final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }
    
    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }
    
    public Long getId() {
        return id;
    }
    
    public TableGroup getTableGroup() {
        return tableGroup;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public boolean isEmpty() {
        return empty;
    }
}
