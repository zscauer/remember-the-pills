package ru.tyumentsev.rememberthepillsbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.tyumentsev.rememberthepillsbot.entity.BotUser;

/**
 * Provides access to registered in database users.
 * 
 * @see BotUser
 */
@Repository
public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    
}
