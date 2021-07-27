package ru.demetrious.animeshikilist.animes;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import ru.demetrious.animeshikilist.FileLoadingTask;
import ru.demetrious.animeshikilist.R;
import ru.demetrious.animeshikilist.activities.MainActivity;
import ru.demetrious.animeshikilist.settings.Settings;

public class Anime {
    private MainActivity mainActivity;
    private ArrayList<AnimeElement> animes = new ArrayList<>();/*Maybe can be deleted*/
    private FileLoadingTask fileLoadingTask;
    private int rolledPosition = -1;

    public Anime(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public int getRolledPosition() {
        return rolledPosition;
    }

    public ArrayList<AnimeElement> getAnimes() {
        return animes;
    }

    public void anime() {
        loadAnimes(false);

        /*animeElements.add(new AnimeElement("Shuumatsu Nani Shitemasu ka? Isogashii Desu ka? Sukutte Moratte Ii Desu ka?", TypeAnime.ONA, 10, 10, 10, 10, StatusAnime.Planned));
        animeElements.add(new AnimeElement("Shuumatsu Nani Shitemasu ka? Isogashii Desu ka? Sukutte Moratte Ii Desu ka?", TypeAnime.ONA, 10, 10, 10, 10, StatusAnime.Planned));
        animeElements.add(new AnimeElement("Shuumatsu Nani Shitemasu ka? Isogashii Desu ka? Sukutte Moratte Ii Desu ka?", TypeAnime.ONA, 10, 10, 10, 10, StatusAnime.Planned));
        animeElementAdapter.notifyDataSetChanged();*/
    }

    public void roll() {
        if (!mainActivity.getAnimeElements().isEmpty()) {
            rolledPosition = new Random().nextInt(mainActivity.getAnimeElements().size());
            Log.e("ERROR", mainActivity.getAnimeElements().get(rolledPosition).getTitle());
            mainActivity.getAnimeElementAdapter().notifyDataSetChanged();
            mainActivity.getAnimeList().smoothScrollToPosition(rolledPosition);
        }
    }

    public void loadAnimes(boolean download) {
        if (fileLoadingTask == null || !fileLoadingTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            fileLoadingTask = new FileLoadingTask(mainActivity, download);
            String nickname = Settings.getData(Settings.DATA_SETTINGS_NICKNAME, "_null_");
            Log.e("Er", download ? "Loading" : "Finding" + " for " + nickname);
            if (!nickname.equals("_null_")) fileLoadingTask.execute(nickname);
        }
    }

    public void filter() {
        mainActivity.getAnimeElements().clear();

        for (AnimeElement anime : animes) {
            if (anime.getType() != null && anime.getStatus() != null) {
                if ((!anime.getStatus().equals(StatusAnime.Planned) || !Settings.getData(Settings.DATA_FILTER_PLANNED, true)) &&
                        (!anime.getStatus().equals(StatusAnime.Watching) || !Settings.getData(Settings.DATA_FILTER_WATCHING, true)) &&
                        (!anime.getStatus().equals(StatusAnime.Rewatching) || !Settings.getData(Settings.DATA_FILTER_REWATCHING, true)) &&
                        (!anime.getStatus().equals(StatusAnime.Completed) || !Settings.getData(Settings.DATA_FILTER_COMPLETED, true)) &&
                        (!anime.getStatus().equals(StatusAnime.OnHold) || !Settings.getData(Settings.DATA_FILTER_ONHOLD, true)) &&
                        (!anime.getStatus().equals(StatusAnime.Dropped) || !Settings.getData(Settings.DATA_FILTER_DROPPED, true))) {
                    continue;
                }
                if ((!anime.getType().equals(TypeAnime.TV) || !Settings.getData(Settings.DATA_FILTER_TV, true)) &&
                        (!anime.getType().equals(TypeAnime.ONA) || !Settings.getData(Settings.DATA_FILTER_ONA, true)) &&
                        (!anime.getType().equals(TypeAnime.OVA) || !Settings.getData(Settings.DATA_FILTER_OVA, true)) &&
                        (!anime.getType().equals(TypeAnime.Movie) || !Settings.getData(Settings.DATA_FILTER_MOVIE, true)) &&
                        (!anime.getType().equals(TypeAnime.Special) || !Settings.getData(Settings.DATA_FILTER_SPECIAL, true)) &&
                        (!anime.getType().equals(TypeAnime.Music) || !Settings.getData(Settings.DATA_FILTER_MUSIC, true))) {
                    continue;
                }
                mainActivity.getAnimeElements().add(anime);
            }
        }
        sort();
        mainActivity.getAnimeElementAdapter().notifyDataSetChanged();
    }

    public void sort() {
        ArrayList<AnimeElement> result = new ArrayList<>();
        int mark1 = 0, mark2 = 0, mark3 = 0, mark4 = 0, mark5 = 0;

        for (AnimeElement animeElement : mainActivity.getAnimeElements()) {
            boolean tmp = true;
            for (int i = getMarkIndex(animeElement.getStatus(), true,
                    0, mark1, mark2, mark3, mark4, mark5, result.size());
                 i < getMarkIndex(animeElement.getStatus(), false,
                         0, mark1, mark2, mark3, mark4, mark5, result.size()); i++) {
                if (sortCondition(result.get(i), animeElement)) {
                    result.add(i, animeElement);
                    tmp = false;
                    break;
                }
            }
            if (tmp)
                result.add(getMarkIndex(animeElement.getStatus(), false,
                        0, mark1, mark2, mark3, mark4, mark5, result.size()), animeElement);
            if (Settings.getData(Settings.DATA_SORT_SEPARATE, true)) {
                switch (animeElement.getStatus()) {
                    case Planned:
                        mark1++;
                    case Watching:
                        mark2++;
                    case Rewatching:
                        mark3++;
                    case Completed:
                        mark4++;
                    case OnHold:
                        mark5++;
                    case Dropped:
                }
            }
        }
        mainActivity.getAnimeElements().clear();
        mainActivity.getAnimeElements().addAll(result);
        mainActivity.getAnimeElementAdapter().notifyDataSetChanged();
    }

    private int getMarkIndex(StatusAnime statusAnime, boolean beginning, int... marks) {
        if (!Settings.getData(Settings.DATA_SORT_SEPARATE, true))
            return beginning ? marks[0] : marks[6];
        switch (statusAnime) {
            case Planned:
                return beginning ? marks[0] : marks[1];
            case Watching:
                return beginning ? marks[1] : marks[2];
            case Rewatching:
                return beginning ? marks[2] : marks[3];
            case Completed:
                return beginning ? marks[3] : marks[4];
            case OnHold:
                return beginning ? marks[4] : marks[5];
            case Dropped:
                return beginning ? marks[5] : marks[6];
            default:
                throw new ExceptionInInitializerError();
        }
    }

    private boolean sortCondition(AnimeElement i, AnimeElement j) {
        float compareTo;

        switch (Settings.getData(Settings.DATA_SORT_CATEGORY, R.id.sort_name)) {
            case R.id.sort_name:
                compareTo = i.getTitle().compareToIgnoreCase(j.getTitle());
                break;
            case R.id.sort_score:
                compareTo = i.getScore() - j.getScore();
                break;
            case R.id.sort_episodes_all:
                compareTo = i.getEpisodesAll() - j.getEpisodesAll();
                break;
            default:
                throw new ExceptionInInitializerError();
        }
        switch (Settings.getData(Settings.DATA_SORT_DIRECTION, R.id.sort_asc)) {
            case R.id.sort_asc:
                return compareTo > 0;
            case R.id.sort_desc:
                return compareTo <= 0;
            default:
                throw new ExceptionInInitializerError();
        }
    }
}
