package site.easy.to.build.crm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class CsvUtils {

    public static List<Map<String, String>> readCsv(InputStream inputStream) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Lire l'en-tête
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return records;
            }

            String[] headers = headerLine.split(",");

            // Lire les lignes de données
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new LinkedHashMap<>();

                for (int i = 0; i < headers.length && i < values.length; i++) {
                    record.put(headers[i].trim(), values[i].trim());
                }

                records.add(record);
            }
        }

        return records;
    }
}