package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return Optional.ofNullable(converter.convert(comment.orElse(null)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(long bookId) {
        List<Comment> comments = commentRepository.findAllByBookId(bookId);
        return comments.stream()
            .map(converter::convert)
            .toList();
    }

    @Transactional
    @Override
    public CommentDto insert(String text, long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(bookId)));
        Comment comment = commentRepository.save(new Comment(0, text, book));
        return converter.convert(comment);
    }

    @Transactional
    @Override
    public CommentDto update(long id, String newText, long newBookId) {
        Book book = bookRepository.findById(newBookId)
            .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(newBookId)));
        Comment comment = commentRepository.save(new Comment(id, newText, book));
        return converter.convert(comment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
