package ru.otus.hw.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;


    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        try {
            List<QuestionDto> question = readCsv(fileName);
            return question.stream()
                .map(QuestionDto::toDomainObject)
                .toList();
        } catch (QuestionReadException e) {
            throw new QuestionReadException(String.format("Error reading file %s", fileName), e);
        }
    }

    private List<QuestionDto> readCsv(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileNotFoundException(String.format("File %s not found", fileName));
            }
            try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 CSVReader reader = configCsvReader(streamReader)) {
                CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .build();
                return csvToBean.parse();
            }
        } catch (FileNotFoundException e) {
            throw new QuestionReadException("File not found", e);
        } catch (IOException e) {
            throw new QuestionReadException("Exception when try close resource", e);
        } catch (Exception e) {
            throw new QuestionReadException("Exception when read csv file", e);
        }
    }

    private CSVReader configCsvReader(InputStreamReader streamReader) {
        CSVParser parser = new CSVParserBuilder()
            .withSeparator(';')
            .build();
        return new CSVReaderBuilder(streamReader)
            .withCSVParser(parser)
            .withSkipLines(1)
            .build();
    }
}
