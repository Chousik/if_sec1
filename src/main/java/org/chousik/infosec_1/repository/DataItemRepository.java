package org.chousik.infosec_1.repository;

import org.chousik.infosec_1.entity.DataItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataItemRepository extends JpaRepository<DataItem, Long> {
}
