package com.example.userservice1b.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import com.example.userservice1b.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ $or: [ " +
           "{ $and: [ { email: { $exists: true, $ne: null, $ne: '' } }, { email: { $regex: ?0, $options: 'i' } } ] }, " +
           "{ $and: [ { name: { $exists: true, $ne: null, $ne: '' } }, { name: { $regex: ?1, $options: 'i' } } ] } " +
           "] }")
    List<User> findByEmailOrNameSafely(String email, String name);

    // ... existing code ...
} 