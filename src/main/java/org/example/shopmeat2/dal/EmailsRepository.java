package org.example.shopmeat2.dal;

import org.example.shopmeat2.modals.Emails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailsRepository extends JpaRepository<Emails, Integer> {
}
