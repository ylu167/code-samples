#include "queue.h"
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

struct queue {
    int size; // Maximum size of the queue
    void **elements; // Array to store elements
    int begin; // Index of the beginning of the queue
    int end; // Index of the end of the queue
    pthread_mutex_t *mutex; // Mutex for thread safety
    pthread_cond_t *not_full_cond; // Condition variable for queue not being full
    pthread_cond_t *not_empty_cond; // Condition variable for queue not being empty
};

queue_t *queue_new(int size) {
    // Allocate memory for the queue structure
    queue_t *q = (queue_t *) malloc(sizeof(queue_t));
    // Initialize queue attributes
    q->size = size;
    q->elements = (void **) malloc(sizeof(void *) * size);
    q->begin = 0;
    q->end = 0;
    q->mutex = (pthread_mutex_t *) malloc(sizeof(pthread_mutex_t));
    q->not_full_cond = (pthread_cond_t *) malloc(sizeof(pthread_cond_t));
    q->not_empty_cond = (pthread_cond_t *) malloc(sizeof(pthread_cond_t));
    // Initialize mutex
    pthread_mutex_init(q->mutex, NULL);
    // Initialize condition variable for not full
    pthread_cond_init(q->not_full_cond, NULL);
    // Initialize condition variable for not empty
    pthread_cond_init(q->not_empty_cond, NULL);
    return q;
}

void queue_delete(queue_t **q) {
    // Free memory for elements array
    free((*q)->elements);
    // Destroy mutex and condition variables
    pthread_mutex_destroy((*q)->mutex);
    pthread_cond_destroy((*q)->not_full_cond);
    pthread_cond_destroy((*q)->not_empty_cond);
    // Free memory for queue structure
    free(*q);
    *q = NULL;
}

bool queue_push(queue_t *q, void *elem) {
    if (q == NULL) {
        return false;
    }
    pthread_mutex_lock(q->mutex);
    // Wait until the queue is not full
    while ((q->end + 1) % q->size == q->begin) {
        pthread_cond_wait(q->not_full_cond, q->mutex);
    }
    // Add element to the queue
    q->elements[q->end] = elem;
    q->end = (q->end + 1) % q->size;

    pthread_mutex_unlock(q->mutex);
    // Signal that queue is not empty
    pthread_cond_broadcast(q->not_empty_cond);
    return true;
}

bool queue_pop(queue_t *q, void **elem) {
    if (q == NULL) {
        return false;
    }
    pthread_mutex_lock(q->mutex);
    // Wait until the queue is not empty
    while (q->begin == q->end) {
        pthread_cond_wait(q->not_empty_cond, q->mutex);
    }
    // Retrieve element from the queue
    *elem = q->elements[q->begin];
    q->begin = (q->begin + 1) % q->size;

    pthread_mutex_unlock(q->mutex);
    // Signal that queue is not full
    pthread_cond_broadcast(q->not_full_cond);

    return true;
}
