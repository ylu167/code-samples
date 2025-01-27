CC = clang
CFLAGS = -Wall -Wextra -Werror -pedantic
SOURCES = memory.c
OBJECTS = $(SOURCES:.c=.o)
EXECUTABLE = memory

all: $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	$(CC) $(CFLAGS) $(OBJECTS) -o $(EXECUTABLE)

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

.PHONY: clean
clean:
	rm -f $(OBJECTS) $(EXECUTABLE)
	