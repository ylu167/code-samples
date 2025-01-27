#include "asgn2_helper_funcs.h"

#include <errno.h>
#include <fcntl.h>
#include <regex.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <unistd.h>

#define BUFSIZE     4096
#define REQEX       "^([a-zA-Z]{1,8}) /([a-zA-Z0-9.-]{1,63}) (HTTP/[0-9]\\.[0-9])\r\n"
#define HEADEX      "([a-zA-Z0-9.-]{1,128}): ([ -~]{1,128})\r\n"
#define OK          "HTTP/1.1 200 OK\r\nContent-Length: 3\r\n\r\nOK\n"
#define CREATED     "HTTP/1.1 201 Created\r\nContent-Length: 8\r\n\r\nCreated\n"
#define BAD_REQUEST "HTTP/1.1 400 Bad Request\r\nContent-Length: 12\r\n\r\nBad Request\n"
#define FORBIDDEN   "HTTP/1.1 403 Forbidden\r\nContent-Length: 10\r\n\r\nForbidden\n"
#define NOT_FOUND   "HTTP/1.1 404 Not Found\r\nContent-Length: 10\r\n\r\nNot Found\n"
#define INTERNAL_SERVER_ERROR                                                                      \
    "HTTP/1.1 500 Internal Server Error\r\nContent-Length: "                                       \
    "22\r\n\r\nInternal Server Error\n"
#define NOT_IMPLEMENTED                                                                            \
    "HTTP/1.1 501 Not Implemented\r\nContent-Length: 16\r\n\r\nNot "                               \
    "Implemented\n"
#define VERSION_NOT_SUPPORTED                                                                      \
    "HTTP/1.1 505 Version Not Supported\r\nContent-Length: 22\r\n\r\nVersion "                     \
    "Not Supported\n"

typedef struct Request {
    int sock_fd; // Socket file descriptor
    char *version; // HTTP version
    char *command; // Command (GET, PUT)
    char *file_name; // File name requested
    char *message_body; // Message body of the request
    int content_length; // Content length of the request
    int remaining_bytes; // Remaining bytes after reading the request
} Request;

int parse_request(Request *request, char *buf, ssize_t bytes_read);
int process_request(Request *request);
int process_get(Request *request);
int process_put(Request *request);

int main(int argc, char *argv[]) {
    if (argc != 2) {
        return EXIT_FAILURE;
    }

    char buf[BUFSIZE + 1] = { '\0' };

    Listener_Socket socket;
    // Convert port number from string to integer
    int port = strtol(argv[1], NULL, 10);
    if (errno == EINVAL) {
        fprintf(stderr, "Invalid Port\n");
        return EXIT_FAILURE;
    }
    if (listener_init(&socket, port) == -1) {
        fprintf(stderr, "Invalid Port\n");
        return EXIT_FAILURE;
    }

    while (true) {
        // Accept incoming connection
        int sock_fd = listener_accept(&socket);
        if (sock_fd == -1) {
            fprintf(stderr, "Unable to Establish Connection\n");
            return (EXIT_FAILURE);
        }
        Request request;
        request.sock_fd = sock_fd;
        // Read the request until the end of headers
        ssize_t bytes_read = read_until(sock_fd, buf, BUFSIZE, "\r\n\r\n");
        if (bytes_read == -1) {
            // Write bad request response if reading fails
            dprintf(request.sock_fd, BAD_REQUEST);
            return EXIT_FAILURE;
        }
        if (parse_request(&request, buf, bytes_read) != EXIT_FAILURE) {
            process_request(&request);
        }
        // Close the socket connection
        close(sock_fd);
        // Clear the buffer
        memset(buf, '\0', sizeof(buf));
    }
    return EXIT_SUCCESS;
}
// Parse the HTTP request
int parse_request(Request *request, char *buf, ssize_t bytes_read) {
    int offset = 0;
    // Regular expression structure
    regex_t re;
    // Array to store matches
    regmatch_t matches[4];
    int rc;
    // Compile the regular expression pattern for HTTP requests
    rc = regcomp(&re, REQEX, REG_EXTENDED);
    // Execute the regular expression matching
    rc = regexec(&re, buf, 4, matches, 0);
    if (rc == 0) {
        // If match found
        // Set the command in the request structure
        request->command = buf;
        // Set the file name in the request structure
        request->file_name = buf + matches[2].rm_so;
        // Set the HTTP version in the request structure
        request->version = buf + matches[3].rm_so;

        buf[matches[1].rm_eo] = '\0';
        request->file_name[matches[2].rm_eo - matches[2].rm_so] = '\0';
        request->version[matches[3].rm_eo - matches[3].rm_so] = '\0';

        buf += matches[3].rm_eo + 2;
        offset += matches[3].rm_eo + 2;
    } else {
        // If no match found
        // Send bad request response
        dprintf(request->sock_fd, BAD_REQUEST);
        // Free the regular expression memory
        regfree(&re);
        return EXIT_FAILURE;
    }
    // Initialize content length to -1
    request->content_length = -1;
    // Compile the regular expression pattern for HTTP headers
    rc = regcomp(&re, HEADEX, REG_EXTENDED);
    // Execute the regular expression matching for headers
    rc = regexec(&re, buf, 3, matches, 0);
    // Loop through all header matches
    while (rc == 0) {
        buf[matches[1].rm_eo] = '\0';
        buf[matches[2].rm_eo] = '\0';
        // If the header is Content-Length
        if (strncmp(buf, "Content-Length", 14) == 0) {
            int value = strtol(buf + matches[2].rm_so, NULL, 10);
            if (errno == EINVAL) {
                dprintf(request->sock_fd, BAD_REQUEST);
            }
            request->content_length = value;
        }
        buf += matches[2].rm_eo + 2;
        offset += matches[2].rm_eo + 2;
        rc = regexec(&re, buf, 3, matches, 0);
    }

    if ((rc != 0) && (buf[0] == '\r' && buf[1] == '\n')) {
        // Set the message body in the request structure
        request->message_body = buf + 2;
        offset += 2;
        // Calculate the remaining bytes after headers
        request->remaining_bytes = bytes_read - offset;
    } else if (rc != 0) {
        dprintf(request->sock_fd, BAD_REQUEST);
        regfree(&re);
        return EXIT_FAILURE;
    }
    regfree(&re);
    return EXIT_SUCCESS;
}

// Process the HTTP request
int process_request(Request *request) {
    // If HTTP version is not supported
    if (strncmp(request->version, "HTTP/1.1", 8) != 0) {
        // Send version not supported response
        dprintf(request->sock_fd, VERSION_NOT_SUPPORTED);
        return EXIT_FAILURE;
    } else if (strncmp(request->command, "GET", 3) == 0) {
        // Process GET request
        return process_get(request);
    } else if (strncmp(request->command, "PUT", 3) == 0) {
        // Process PUT request
        return process_put(request);
    } else {
        // Send not implemented response
        dprintf(request->sock_fd, NOT_IMPLEMENTED);
        return EXIT_FAILURE;
    }
}
// Process the GET request
int process_get(Request *request) {
    // If content length is specified in GET request
    if (request->content_length != -1) {
        // Send bad request response
        dprintf(request->sock_fd, BAD_REQUEST);
        return EXIT_FAILURE;
    }
    // If remaining bytes are present after headers
    if (request->remaining_bytes > 0) {
        // Send bad request response
        dprintf(request->sock_fd, BAD_REQUEST);
        return EXIT_FAILURE;
    }

    int fd; // File descriptor
    // If file is a directory
    if ((fd = open(request->file_name, O_RDONLY | O_DIRECTORY)) != -1) {
        // Send forbidden response
        dprintf(request->sock_fd, FORBIDDEN);
        return EXIT_FAILURE;
    }
    // If file cannot be opened
    if ((fd = open(request->file_name, O_RDONLY)) == -1) {
        // If file not found
        if (errno == ENOENT) {
            // Send not found response
            dprintf(request->sock_fd, NOT_FOUND);
            // If access is denied
        } else if (errno == EACCES) {
            // Send forbidden response
            dprintf(request->sock_fd, FORBIDDEN);
        } else {
            // Send internal server error response
            dprintf(request->sock_fd, INTERNAL_SERVER_ERROR);
        }
        return EXIT_FAILURE;
    }
    // File status structure
    struct stat st;
    // Get file status
    fstat(fd, &st);
    // Get file size
    off_t size = st.st_size;
    // Send OK response with content length
    dprintf(request->sock_fd, "HTTP/1.1 200 OK\r\nContent-Length: %ld\r\n\r\n", size);
    // Write file contents to socket
    int bytes_written = pass_n_bytes(fd, request->sock_fd, size);
    // If error in writing
    if (bytes_written == -1) {
        // Send internal server error response
        dprintf(request->sock_fd, INTERNAL_SERVER_ERROR);
        return EXIT_FAILURE;
    }
    // Close the file descriptor
    close(fd);
    return EXIT_SUCCESS;
}
// Process the PUT request
int process_put(Request *request) {
    // If content length is not specified in PUT request
    if (request->content_length == -1) {
        // Send bad request response
        dprintf(request->sock_fd, BAD_REQUEST);
        return EXIT_FAILURE;
    }
    int fd;
    int status_code = 0;
    // If file is a directory
    if ((fd = open(request->file_name, O_WRONLY | O_DIRECTORY, 0666)) != -1) {
        // Send forbidden response
        dprintf(request->sock_fd, FORBIDDEN);
        return EXIT_FAILURE;
    }
    // If file cannot be opened or created
    if ((fd = open(request->file_name, O_WRONLY | O_CREAT | O_EXCL, 0666)) == -1) {
        // If file already exists
        if (errno == EEXIST) {
            // Set status code to 200
            status_code = 200;
            // If access is denied
        } else if (errno == EACCES) {
            // Send forbidden response
            dprintf(request->sock_fd, FORBIDDEN);
            return EXIT_FAILURE;
        } else {
            // Send internal server error response
            dprintf(request->sock_fd, INTERNAL_SERVER_ERROR);
            return EXIT_FAILURE;
        }
        // If file is created successfully
    } else if (fd != -1) {
        // Set status code to 201
        status_code = 201;
    }
    // If file already exists
    if (status_code == 200) {
        if ((fd = open(request->file_name, O_WRONLY | O_CREAT | O_TRUNC, 0666)) == -1) {
            if (errno == EACCES) {
                dprintf(request->sock_fd, FORBIDDEN);
                return EXIT_FAILURE;
            } else {
                dprintf(request->sock_fd, INTERNAL_SERVER_ERROR);
                return EXIT_FAILURE;
            }
        }
    }
    // Write message body to file
    int bytes = write_n_bytes(fd, request->message_body, request->remaining_bytes);
    // If error in writing
    if (bytes == -1) {
        // Send internal server error response
        dprintf(request->sock_fd, INTERNAL_SERVER_ERROR);
        return EXIT_FAILURE;
    }
    // Calculate size of remaining data
    int remaining = request->content_length - request->remaining_bytes;
    // Write remaining data to file
    bytes = pass_n_bytes(request->sock_fd, fd, remaining);
    // If error in writing
    if (bytes == -1) {
        dprintf(request->sock_fd, INTERNAL_SERVER_ERROR);
        return EXIT_FAILURE;
    }

    if (status_code == 201) {
        dprintf(request->sock_fd, CREATED);
    } else {
        dprintf(request->sock_fd, OK);
    }
    close(fd);
    return EXIT_SUCCESS;
}
