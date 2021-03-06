package fr.lumen.motus;

public class Matcher {
    private final String word;

    public Matcher(String word) {
        this.word = word;
    }

    public String result(String proposition) {
        assert word.length() == proposition.length();
        StringBuilder result = new StringBuilder(word.length());
        for (int i = 0; i < word.length(); i++) {
            final char c = proposition.charAt(i);
            if (word.charAt(i) == c) {
                result.append('R');
            } else if (containsBadPlaced(c, proposition)) {
                result.append('J');
            } else {
                result.append('B');
            }
        }
        return result.toString();
    }

    private boolean containsBadPlaced(char c, String proposition) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == c && proposition.charAt(i) != c) {
                return true;
            }
        }
        return false;
    }

    public String getWord() {
        return word;
    }

}
