package site.easy.to.build.crm.util.csv;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class CustomDoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            String value = p.getText()
                    .replace(',', '.')
                    .replaceAll("[^\\d.]", ""); // Supprime tous les caractères non numériques sauf le point
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid number format: " + p.getText(), e);
        }
    }
}