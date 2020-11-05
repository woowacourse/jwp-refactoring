package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;

import kitchenpos.builder.TableGroupBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables;

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public TableGroupBuilder toBuilder() {
        return new TableGroupBuilder(id, createdDate, orderTables);
    }
}
