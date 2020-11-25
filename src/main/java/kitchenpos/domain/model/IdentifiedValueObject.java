package kitchenpos.domain.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdentifiedValueObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    protected IdentifiedValueObject() {
    }

    IdentifiedValueObject(Long seq) {
        this.seq = seq;
    }

    Long getSeq() {
        return seq;
    }
}
