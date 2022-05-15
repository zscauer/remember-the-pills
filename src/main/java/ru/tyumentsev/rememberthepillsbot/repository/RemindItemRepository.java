package ru.tyumentsev.rememberthepillsbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;

/**
 * Provides access to saved in database reminds.
 * 
 * @see RemindItem
 */
@Repository
public interface RemindItemRepository extends JpaRepository<RemindItem, Long> {
    
}
