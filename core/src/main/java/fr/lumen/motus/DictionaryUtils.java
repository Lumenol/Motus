package fr.lumen.motus;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DictionaryUtils {
    private DictionaryUtils() {
    }

    public static List<String> clean(Stream<String> words) {
        return words.map(String::trim).map(String::toUpperCase)
                .filter(w -> w.chars().allMatch(Character::isUpperCase))
                .sorted().distinct().collect(Collectors.toList());
    }

    public static List<String> getEmbedded() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(DictionaryUtils.class.getResourceAsStream("/words.txt")))) {
            return clean(reader.lines());
        }
    }
}
