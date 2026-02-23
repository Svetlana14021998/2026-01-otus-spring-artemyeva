package ru.otus.hw.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ResourceUtils;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        try {
            File csvFile = ResourceUtils.getFile("classpath:" + fileName);
            CsvToBean<QuestionDto> csvToBean = configureCsvToBean(csvFile);
            List<QuestionDto> question = csvToBean.parse();

            return question.stream()
                .map(QuestionDto::toDomainObject)
                .toList();
        } catch (FileNotFoundException e) {
            throw new QuestionReadException("Can`t read file", e);
        }
    }

    private CsvToBean<QuestionDto> configureCsvToBean(File csvFile) throws FileNotFoundException {
        FileReader fileReader = new FileReader(csvFile);

        CSVParser parser = new CSVParserBuilder()
            .withSeparator(';')
            .build();

        CSVReader reader = new CSVReaderBuilder(fileReader)
            .withCSVParser(parser)
            .withSkipLines(1)
            .build();

        return new CsvToBeanBuilder<QuestionDto>(reader)
            .withType(QuestionDto.class)
            .build();
    }
}
