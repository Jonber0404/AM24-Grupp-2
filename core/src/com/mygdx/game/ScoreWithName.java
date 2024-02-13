package com.mygdx.game;

/**
 * Record som sparar ett score och namnet på spelaren
 * @param name
 * @param score
 */
public record ScoreWithName(String name, int score) implements Comparable<ScoreWithName> {

    /**
     * Jämför poängen "baklänges", så att högsta poäng hamnar först i listan.
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(ScoreWithName o) {
        return Integer.compare(this.score, o.score) * -1;
    }

    @Override
    public String toString() {
        return String.format("%s: %d", name, score);
    }
}
