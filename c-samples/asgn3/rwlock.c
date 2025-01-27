#include "rwlock.h"
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

struct rwlock {
    pthread_mutex_t mutex; // Mutex for protecting the shared state of the lock
    pthread_cond_t readers_cond; // Condition variable for readers
    pthread_cond_t writers_cond; // Condition variable for writers
    uint32_t readers_count; // Number of active readers
    uint32_t writers_count; // Number of active writers
    PRIORITY priority_mode; // Priority mode of the lock
    uint32_t n; // Value of n if using N_WAY priority mode
};

rwlock_t *rwlock_new(PRIORITY p, uint32_t n) {
    // Allocate memory for the rwlock structure
    rwlock_t *rw = (rwlock_t *) malloc(sizeof(rwlock_t));
    // Initialize mutex and condition variables
    pthread_mutex_init(&rw->mutex, NULL);
    pthread_cond_init(&rw->readers_cond, NULL);
    pthread_cond_init(&rw->writers_cond, NULL);
    // Initialize counts and priority mode
    rw->readers_count = 0;
    rw->writers_count = 0;
    rw->priority_mode = p;
    rw->n = n;
    return rw;
}

void rwlock_delete(rwlock_t **rw) {
    if (rw == NULL || *rw == NULL) {
        return;
    }
    // Destroy mutex and condition variables
    pthread_mutex_destroy(&(*rw)->mutex);
    pthread_cond_destroy(&(*rw)->readers_cond);
    pthread_cond_destroy(&(*rw)->writers_cond);
    // Free memory
    free(*rw);
    *rw = NULL;
}

void reader_lock(rwlock_t *rw) {
    pthread_mutex_lock(&rw->mutex);
    // Wait for the appropriate conditions based on the priority mode
    while (rw->priority_mode == WRITERS && rw->writers_count > 0) {
        pthread_cond_wait(&rw->readers_cond, &rw->mutex);
    }
    while ((rw->writers_count > 0 || (rw->priority_mode == N_WAY && rw->readers_count >= rw->n))) {
        pthread_cond_wait(&rw->readers_cond, &rw->mutex);
    }
    // Increment the number of active readers
    rw->readers_count++;
    pthread_mutex_unlock(&rw->mutex);
}

void reader_unlock(rwlock_t *rw) {
    pthread_mutex_lock(&rw->mutex);
    // Decrement the number of active readers
    rw->readers_count--;
    // Signal writers if no active readers
    if (rw->readers_count == 0) {
        pthread_cond_signal(&rw->writers_cond);
    }
    pthread_mutex_unlock(&rw->mutex);
}

void writer_lock(rwlock_t *rw) {
    pthread_mutex_lock(&rw->mutex);
    // Increment the number of active writers
    rw->writers_count++;
    // Wait until no readers or writers are active
    while (rw->readers_count > 0 || rw->writers_count > 1) {
        pthread_cond_wait(&rw->writers_cond, &rw->mutex);
    }
    pthread_mutex_unlock(&rw->mutex);
}

void writer_unlock(rwlock_t *rw) {
    pthread_mutex_lock(&rw->mutex);
    // Decrement the number of active writers
    rw->writers_count--;
    // Signal writers and readers
    pthread_cond_signal(&rw->writers_cond);
    pthread_cond_broadcast(&rw->readers_cond);
    pthread_mutex_unlock(&rw->mutex);
}
