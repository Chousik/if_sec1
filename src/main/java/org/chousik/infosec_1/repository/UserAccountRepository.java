package org.chousik.infosec_1.repository;

import org.chousik.infosec_1.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
