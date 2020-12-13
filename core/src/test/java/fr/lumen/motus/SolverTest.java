package fr.lumen.motus;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SolverTest {
    private static List<String> words() {
        return List.of("ACCUS", "BONNE", "CIBLA", "CREPU", "CROIS", "FILET", "GRECS", "HANTE", "JIMMY", "LYCEE", "NUAGE", "PELER", "PIQUE", "QUART", "ROUND", "SEIDE", "TANGO", "TIREE", "TONUS", "VOUTE");
    }

    @Test
    void oneSolution() {
        final Solver solver = new Solver(words(), 5, 'A');
        assertThat(solver.allSolutions()).containsExactly("ACCUS");
    }

    @Test
    void withPropositions() {
        final Solver solver = new Solver(words(), 5, 'C');

        assertThat(solver.allSolutions()).containsExactlyInAnyOrder("CIBLA", "CREPU", "CROIS");

        solver.addProposition("CRXXX", "RRBBB");
        assertThat(solver.allSolutions()).containsExactlyInAnyOrder("CREPU", "CROIS");

        solver.addProposition("IXXXX", "JBBBB");
        assertThat(solver.allSolutions()).containsExactlyInAnyOrder("CROIS");
    }
}
