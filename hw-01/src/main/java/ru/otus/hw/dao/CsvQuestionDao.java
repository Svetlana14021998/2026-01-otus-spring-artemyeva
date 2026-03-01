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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() throws RuntimeException {
        String fileName = fileNameProvider.getTestFileName();
        try {
            File csvFile = getResourceAsFile(fileName);
            List<QuestionDto> question = configureAndReadCsv(csvFile);

            return question.stream()
                .map(QuestionDto::toDomainObject)
                .toList();
        } catch (FileNotFoundException e) {
            throw new QuestionReadException(String.format("File %s not found", fileName), e);
        } catch (IOException e) {
            throw new QuestionReadException(String.format("Can`t read file %s", fileName), e);
        }
    }

    private List<QuestionDto> configureAndReadCsv(File csvFile) throws IOException {
        try (FileReader fileReader = new FileReader(csvFile)) {
            CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

            CSVReader reader = new CSVReaderBuilder(fileReader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();

            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                .withType(QuestionDto.class)
                .build();
            return csvToBean.parse();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public File getResourceAsFile(String path) throws IOException {
        String filename = Paths.get(path).getFileName().toString();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

        if (inputStream == null) {
            throw new FileNotFoundException();
        }

        File tempFile = File.createTempFile("temp_", filename);
        tempFile.deleteOnExit();

        try (inputStream; FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            inputStream.transferTo(outputStream);
        }
        return tempFile;
    }
}