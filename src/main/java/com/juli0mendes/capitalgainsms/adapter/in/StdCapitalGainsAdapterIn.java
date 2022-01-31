package com.juli0mendes.capitalgainsms.adapter.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juli0mendes.capitalgainsms.application.ports.in.CapitalGainsPortIn;
import com.juli0mendes.capitalgainsms.application.ports.in.OperationDto;
import com.juli0mendes.capitalgainsms.application.ports.out.TaxDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.juli0mendes.capitalgainsms.application.common.ScapeUtil.scape;

@Component
public class StdCapitalGainsAdapterIn {

    private static final Logger log = LoggerFactory.getLogger(StdCapitalGainsAdapterIn.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final ObjectMapper objectMapper;

    private final CapitalGainsPortIn capitalGainsPortIn;

    public StdCapitalGainsAdapterIn(final ObjectMapper objectMapper,
                                    final CapitalGainsPortIn capitalGainsPortIn) {
        this.objectMapper = objectMapper;
        this.capitalGainsPortIn = capitalGainsPortIn;
    }

    private static boolean isLineEmpty(String line) {
        if (line != null && !line.trim().isEmpty())
            return false;
        return true;
    }

    @Scheduled(fixedRate = 5000)
    public void calculateTax() throws JsonProcessingException {

        log.info("calculate-tax; start; system;");

        var operations = new ArrayList<OperationDto>();

        try {
            var linesFileInput = this.readFile();

            for (String line : linesFileInput) {
                var operationDtos = this.objectMapper.readValue(line, OperationDto[].class);
                log.info("calculate-tax; status; size-array=\"{}\";", operationDtos.length);

                for (var i = 0; i < operationDtos.length; i++) {
                    operations.add(operationDtos[i]);
                }
            }
            log.debug("lines list = {}", operations);

            List<TaxDto> taxesDto = this.capitalGainsPortIn.calculateTax(operations);

            log.info("taxes list = {}", taxesDto);

            var linesFileOutput = this.objectMapper.writeValueAsString(taxesDto);
            this.writeFile(linesFileOutput);

            log.info("calculate-tax; end; system;");
        } catch (IOException e) {
            log.error("calculate-tax; exception; system; exception={\"{}\";", scape(e));
            e.printStackTrace();
        }

    }

    private List<String> readFile() throws IOException {
        Path path = Paths.get("./src/main/resources/fileIn.txt");
        String read = Files.readAllLines(path).get(0);

        List<String> linesFile = Files.readAllLines(path);
        linesFile.removeIf(lineFile -> isLineEmpty(lineFile));
        return linesFile;
    }

    public void writeFile(String linesFile) throws IOException {
        Path path = Paths.get("./src/main/resources/fileOut.txt");
        Files.write(path, linesFile.getBytes());
    }
}
