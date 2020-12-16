package fr.lumen.motus.web;

import fr.lumen.motus.DictionaryUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@ApplicationScoped
public class Producers {

    @Produces
    @Dictionary
    public List<String> getDictionary() {
        try {
            return DictionaryUtils.getEmbedded();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
