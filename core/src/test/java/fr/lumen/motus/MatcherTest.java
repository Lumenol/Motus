package fr.lumen.motus;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


class MatcherTest {

    @ParameterizedTest(name = "{index} : {0} | {1} -> {2}")
    @CsvSource(value = {"A,A,R", "A,B,B", "AB,BA,JJ", "ABADEFGH,ABCAEAGG,RRBJRJRB", "BANANE,XXXBXX,BBBJBB", "BANANE,BXXBXX,RBBBBB"})
    void result(String word, String proposition, String expected) {
        final Matcher matcher = new Matcher(word);
        final String result = matcher.result(proposition);
        assertThat(result).isEqualTo(expected);
    }
}
