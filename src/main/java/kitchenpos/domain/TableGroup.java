package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @CreatedDate
    private LocalDateTime createdDate;
    
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;
    
    public TableGroup(final LocalDateTime createdDate,
                      final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }
    
    public TableGroup(final Long id,
                      final LocalDateTime createdDate,
                      final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }
    
    public Long getId() {
        return id;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
