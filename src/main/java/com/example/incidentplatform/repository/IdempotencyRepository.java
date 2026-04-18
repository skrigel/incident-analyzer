package com.example.incidentplatform.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Arrays;

/**
 * Redis-backed store for idempotency keys.
 * Used by Kafka consumers in Stage 3 to detect redeliveries.
 *
 * Key format:  "idem:{entity}:{operation}:{id}"
 * TTL:         5 minutes — long enough to cover Kafka redelivery windows,
 *              short enough not to bloat Redis memory.
 */
@Repository
@RequiredArgsConstructor
public class IdempotencyRepository {

    private static final String   KEY_PREFIX = "idem:";
    private static final Duration TTL        = Duration.ofMinutes(5);

    private final StringRedisTemplate redisTemplate;

    /**
     * Atomically sets the key if it doesn't exist.
     *
     * @return true if key was set (first time), false if already exists
     */
    public boolean setIfAbsent(String key, String value) {
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(prefixKey(key), value, TTL);
        return Boolean.TRUE.equals(result);
    }

    /**
     * Check if idempotency key exists (operation already processed).
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(prefixKey(key)));
    }

    /**
     * Get value stored with key (for retrieving cached results).
     *
     * @return stored value, or null if not found
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(prefixKey(key));
    }

    /**
     * Delete idempotency key (allow retry after failure).
     */
    public void delete(String key) {
        redisTemplate.delete(prefixKey(key));
    }

    /**
     * Prefix all keys with "idem:" namespace.
     */
    private String prefixKey(String key) {
        if (key.startsWith(KEY_PREFIX)) {
            return key;  // Already prefixed
        }
        return KEY_PREFIX + key;
    }

    /**
     * Helper to build idempotency keys with consistent format.
     * Accepts variable number of parts for maximum flexibility.
     *
     * @param parts key components (e.g., "alert", "ingest", "abc-123")
     * @return formatted key with parts joined by colons
     *
     * Example: buildKey("alert", "ingest", "abc-123")
     *          → "alert:ingest:abc-123"
     *          (will be stored in Redis as "idem:alert:ingest:abc-123")
     *
     * Example: buildKey(incidentId, "summary")
     *          → "550e8400-e29b-41d4-a716-446655440000:summary"
     */
    public static String buildKey(Object... parts) {
        return String.join(":",
                Arrays.stream(parts)
                        .map(Object::toString)
                        .toArray(String[]::new));
    }
}