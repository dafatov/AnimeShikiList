package ru.demetrious.animeshikilist.animes;

public class AnimeElement {
    private String title;
    private TypeAnime type;
    private int episodesAll;
    private int episodesSeen;
    private int episodesSeenCount;
    private int score;
    private StatusAnime status;

    public AnimeElement(String title, TypeAnime type, int episodesAll, int episodesSeen, int episodesSeenCount, int score, StatusAnime status) {
        setTitle(title);
        setType(type);
        setEpisodesAll(episodesAll);
        setEpisodesSeen(episodesSeen, episodesAll);
        setEpisodesSeenCount(episodesSeenCount);
        setScore(score);
        setStatus(status);
    }

    public AnimeElement() {
    }

    public void setEpisodesSeen(int episodesSeen, int episodesAll) {
        if (episodesSeen >= 0/* && episodesSeen <= episodesAll*/) {
            this.episodesSeen = episodesSeen;
        } else throw new ExceptionInInitializerError();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TypeAnime getType() {
        return type;
    }

    public void setType(TypeAnime type) {
        this.type = type;
    }

    public int getEpisodesAll() {
        return episodesAll;
    }

    public void setEpisodesAll(int episodesAll) {
        if (episodesAll >= 0) {
            this.episodesAll = episodesAll;
        } else throw new ExceptionInInitializerError();
    }

    public int getEpisodesSeen() {
        return episodesSeen;
    }

    public int getEpisodesSeenCount() {
        return episodesSeenCount;
    }

    public void setEpisodesSeenCount(int episodesSeenCount) {
        if (episodesSeenCount >= 0) {
            this.episodesSeenCount = episodesSeenCount;
        } else throw new ExceptionInInitializerError();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score >= 0 && score <= 10) {
            this.score = score;
        } else throw new ExceptionWrongScore(score);
    }

    public StatusAnime getStatus() {
        return status;
    }

    public void setStatus(StatusAnime status) {
        this.status = status;
    }

    class ExceptionWrongScore extends ExceptionInInitializerError {
        ExceptionWrongScore(int score) {
            super("Score must be among 0 and 10 but now score is " + score);
        }
    }
}
