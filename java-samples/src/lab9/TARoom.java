package lab9;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.*;

public class TARoom {
    private Queue<Student> studentQ = new LinkedList<>();
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    public void putStudent(Student student) {
        lock.lock();
        try {
            studentQ.add(student);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public Student getStudent() throws InterruptedException {
        lock.lock();
        try {
            while (studentQ.isEmpty() && !JavaCourse.allDone) {
                notEmpty.await();
            }
            if (JavaCourse.allDone && studentQ.isEmpty()) {
                return null;
            }
            return studentQ.poll();
        } finally {
            lock.unlock();
        }
    }
    public int getQueueLength() {
        lock.lock();
        try {
            return studentQ.size();
        } finally {
            lock.unlock();
        }
    }
}
