package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;

/**
 * Implementation of the custom user repository.
 * This handles null inputs more gracefully than the standard repository methods.
 */
public class CustomUserRepositoryImpl implements CustomUserRepository {
    
    private static final Logger log = LoggerFactory.getLogger(CustomUserRepositoryImpl.class);
    private final MongoTemplate mongoTemplate;
    
    public CustomUserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public List<UserEntity> findByEmailOrNameSafely(String email, String name) {
        log.info("Searching for users with email: {} or name: {}", email, name);
        
        Query query = new Query();
        
        // If both parameters are null or empty, return all users
        if ((email == null || email.trim().isEmpty()) && (name == null || name.trim().isEmpty())) {
            log.info("Both email and name are null or empty, returning all users");
            return mongoTemplate.findAll(UserEntity.class);
        }
        
        // Build criteria
        Criteria criteria = new Criteria();
        
        if (email != null && !email.trim().isEmpty()) {
            criteria.orOperator(
                Criteria.where("email").regex(email, "i")
            );
        }
        
        if (name != null && !name.trim().isEmpty()) {
            if (email != null && !email.trim().isEmpty()) {
                // If email was already added, we need to update the criteria
                criteria = criteria.orOperator(
                    Criteria.where("email").regex(email, "i"),
                    Criteria.where("name").regex(name, "i")
                );
            } else {
                // If only name is valid
                criteria = criteria.orOperator(
                    Criteria.where("name").regex(name, "i")
                );
            }
        }
        
        query.addCriteria(criteria);
        log.debug("Executing query: {}", query);
        
        try {
            List<UserEntity> results = mongoTemplate.find(query, UserEntity.class);
            log.info("Found {} users matching the criteria", results.size());
            return results;
        } catch (Exception e) {
            log.error("Error searching for users: {}", e.getMessage());
            // Fallback to returning all users
            log.info("Falling back to returning all users");
            return mongoTemplate.findAll(UserEntity.class);
        }
    }
} 