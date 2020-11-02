package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "TABLE_GROUP_ID"))
public class TableGroup extends BaseEntity {
    @CreatedDate
    private LocalDateTime createdDate;
}
