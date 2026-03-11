package dev.practice.shopapp.repositories;

import dev.practice.shopapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Why we use "LEFT JOIN FETCH":
     * 1. Solving the "N+1" Problem: By default, JPA loads the User first,
     *    then runs a separate SQL query for EVERY user to get their addresses.
     *    (100 users = 101 queries). This "FETCH" joins them into 1 single query.
     *
     * 2. Performance: It tells Hibernate to preload the 'addresses' collection
     *    immediately, so it's ready when Jackson turns the List into JSON.
     *
     * 3. LEFT JOIN: Ensures we still get Users even if they have zero addresses.
     */
// 1. Your Custom Search with Fetch Join
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE " +
            "CAST(u.id AS string) LIKE %:term% OR " + // Added casting for the ID!
            "LOWER(u.firstName) LIKE %:term% OR " +
            "LOWER(u.lastName) LIKE %:term% OR " +
            "LOWER(u.email) LIKE %:term%")
    Page<User> findBySearchTerm(String term, Pageable pageable);

    // 2. Your "All Users" with Fetch Join + Pagination
    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.addresses",
            countQuery = "SELECT count(u) FROM User u")
    Page<User> findAllWithAddresses(Pageable pageable);

    // FETCH joined to get the user and their addresses in 1 hit
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :id")
    Optional<User> findByIdWithAddresses(@Param("id") Long id);

    Optional<User> findByEmail(String email);
}
