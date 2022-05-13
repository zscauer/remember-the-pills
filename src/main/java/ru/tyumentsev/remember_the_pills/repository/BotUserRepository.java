package ru.tyumentsev.remember_the_pills.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.tyumentsev.remember_the_pills.entity.BotUser;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    
}
