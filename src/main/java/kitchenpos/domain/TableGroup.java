package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

@Table(name="table_group")
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    public TableGroup() {}

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }
}
