package ru.otus.hw.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
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
        } catch (IOException e) {
            throw new QuestionReadException(String.format("Error reading file %s", fileName), e);
        }
    }

    private List<QuestionDto> readCsv(String fileName) throws IOException {
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        CSVReader reader = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException(String.format("File %s not found", fileName));
            }
            streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            reader = configCsvReader(streamReader);
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                .withType(QuestionDto.class)
                .build();
            return csvToBean.parse();
        } finally {
            closeResource(reader);
            closeResource(streamReader);
            closeResource(inputStream);
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

    private void closeResource(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + closeable.getClass().getSimpleName());
            }
        }
    }
}