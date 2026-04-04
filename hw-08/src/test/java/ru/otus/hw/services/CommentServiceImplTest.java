package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.converters.Comment2CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка работы CommentServiceImpl")
public class CommentServiceImplTest extends AbstractServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private Comment2CommentDtoConverter commentDtoConverter;

    @Test
    @DisplayName("Поиск комментария по id")
    void findByIdTest() {
        // given
        String id = "1";

        // when
        Optional<CommentDto> comment = commentService.findById(id);

        //then
        assertThat(comment).isPresent();

        CommentDto commentDto = comment.get();

        CommentDto expectedComment = commentDtoConverter.convert(mongoTemplate.findById(id, Comment.class));

        assertThat(commentDto)
            .usingRecursiveComparison()
            .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Поиск всех комментариев по id книги")
    void findAllByBookIdTest() {
        // given
        String bookId = "1";

        // when
        List<CommentDto> comments = commentService.findAllByBookId(bookId);

        //then
        Query query = new Query(Criteria.where("bookId").is(bookId));
        List<CommentDto> expectedComments = mongoTemplate.find(query, Comment.class).stream()
            .map(x -> commentDtoConverter.convert(x))
            .toList();

        assertThat(comments)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(expectedComments);
    }

    @Test
    @DisplayName("Сохранение комментария")
    void saveTest() {
        // given
        // when
        CommentDto newComment = commentService.insert("new Comment", "1");

        //then
        CommentDto comment = commentDtoConverter.convert(mongoTemplate.findById(newComment.getId(), Comment.class));

        assertThat(newComment)
            .usingRecursiveComparison()
            .isEqualTo(comment);
    }

    @Test
    @DisplayName("Изменение комментария")
    void updateTest() {
        // given
        String id = "1";

        // when
        CommentDto updateComment = commentService.update(id, "update Book", "2");

        //then
        CommentDto comment = commentDtoConverter.convert(mongoTemplate.findById(id, Comment.class));

        assertThat(updateComment)
            .usingRecursiveComparison()
            .isEqualTo(comment);
    }

    @Test
    @DisplayName("Удаление комментария")
    void deleteTest() {
        // given
        String id = "1";

        // when
        commentService.deleteById(id);

        //then
        Comment comment = mongoTemplate.findById(id, Comment.class);

        assertThat(comment).isNull();
    }
}
