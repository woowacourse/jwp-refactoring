package kitchenpos.repository.entity;

import java.util.Collection;

public interface MenuEntityRepository {
    boolean existsAllByInIn(Collection<Long> ids);
}
