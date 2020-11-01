package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "TABLE_GROUP_ID"))
public class TableGroup extends BaseEntity { // 여러 테이블 한꺼번에 계산할 때
    @CreatedDate
    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
