package fr.lumen.motus.web;

class SolveAnswer {
    private final boolean solution;
    private final String word;

    public SolveAnswer(boolean solution, String word) {
        this.solution = solution;
        this.word = word;
    }

    public boolean isSolution() {
        return solution;
    }

    public String getWord() {
        return word;
    }
}
