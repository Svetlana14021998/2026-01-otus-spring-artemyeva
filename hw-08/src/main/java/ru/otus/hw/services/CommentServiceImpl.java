package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.Comment2CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Comment2CommentDtoConverter converter;

    @Override
    public Optional<CommentDto> findById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return Optional.ofNullable(converter.convert(comment.orElse(null)));
    }

    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        List<Comment> comments = commentRepository.findAllByBookId(bookId);
        return comments.stream()
            .map(converter::convert)
            .toList();
    }

    @Override
    public CommentDto insert(String text, String bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        Comment comment = commentRepository.save(new Comment(null, text, book));
        return converter.convert(comment);
    }

    @Override
    public CommentDto update(String id, String newText, String newBookId) {
        Book book = bookRepository.findById(newBookId)
            .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(newBookId)));
        Comment comment = commentRepository.save(new Comment(id, newText, book));
        return converter.convert(comment);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
