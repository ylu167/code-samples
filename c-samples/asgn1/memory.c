#include <fcntl.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define MAX_BUF   4096
#define MIN(a, b) ((a) < (b) ? (a) : (b))

int read_line(char *buf, int size) {
    int i = 0, bytes_read;
    while (i < size - 1 && (bytes_read = read(STDIN_FILENO, buf + i, 1)) > 0) {
        if (buf[i] == '\n') {
            buf[i] = '\0';
            return i;
        }
        i++;
    }
    if (bytes_read <= 0)
        return -1; // Error or EOF
    buf[i] = '\0';
    return i;
}

int parse_num(const char *buf) {
    char *end;
    int num = strtoll(buf, &end, 10);
    if (end == buf || *end != '\0' || num < 0)
        return -1;
    return num;
}

int get_operation(const char *location) {
    int fd = open(location, O_RDONLY);
    if (fd == -1) {
        dprintf(STDERR_FILENO, "Invalid Command\n");
        return 1;
    }

    char buf[MAX_BUF];
    int bytes_read;
    while ((bytes_read = read(fd, buf, MAX_BUF)) > 0) {
        if (write(STDOUT_FILENO, buf, bytes_read) != bytes_read) {
            close(fd);
            dprintf(STDERR_FILENO, "Operation Failed\n");
            return 1;
        }
    }
    close(fd);
    if (bytes_read == -1) {
        dprintf(STDERR_FILENO, "Operation Failed\n");
        return 1;
    }
    return 0;
}

int set_operation(const char *location, int content_len) {
    int fd = open(location, O_WRONLY | O_CREAT | O_TRUNC, 0666);
    if (fd == -1) {
        dprintf(STDERR_FILENO, "Operation Failed\n");
        return 1;
    }

    char buf[MAX_BUF];
    int total_written = 0;
    int bytes_read;
    while (
        total_written < content_len
        && (bytes_read = read(STDIN_FILENO, buf, MIN(MAX_BUF, content_len - total_written))) > 0) {
        if (write(fd, buf, bytes_read) != bytes_read) {
            close(fd);
            dprintf(STDERR_FILENO, "Operation Failed\n");
            return 1;
        }
        total_written += bytes_read;
    }
    close(fd);
    if (bytes_read == -1 || total_written < content_len) {
        dprintf(STDERR_FILENO, "Operation Failed\n");
        return 1;
    }
    dprintf(STDOUT_FILENO, "OK\n");
    return 0;
}

int main() {
    char cmd[MAX_BUF], location[PATH_MAX];
    if (read_line(cmd, sizeof(cmd)) <= 0 || read_line(location, sizeof(location)) <= 0) {
        dprintf(STDERR_FILENO, "Invalid Command\n");
        return 1;
    }

    if (strcmp(cmd, "get") == 0) {
        return get_operation(location);
    } else if (strcmp(cmd, "set") == 0) {
        char len_str[MAX_BUF];
        if (read_line(len_str, sizeof(len_str)) <= 0) {
            dprintf(STDERR_FILENO, "Invalid Command\n");
            return 1;
        }
        long long content_len = parse_num(len_str);
        if (content_len == -1) {
            dprintf(STDERR_FILENO, "Invalid Command\n");
            return 1;
        }
        return set_operation(location, content_len);
    } else {
        dprintf(STDERR_FILENO, "Invalid Command\n");
        return 1;
    }
}
