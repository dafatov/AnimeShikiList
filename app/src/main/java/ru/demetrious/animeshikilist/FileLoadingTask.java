package ru.demetrious.animeshikilist;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import ru.demetrious.animeshikilist.activities.MainActivity;
import ru.demetrious.animeshikilist.animes.AnimeElement;
import ru.demetrious.animeshikilist.animes.StatusAnime;
import ru.demetrious.animeshikilist.animes.TypeAnime;


public class FileLoadingTask extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;
    private AnimeElement currentAnime = new AnimeElement();
    private boolean download;

    public FileLoadingTask(MainActivity mainActivity, boolean download) {
        this.mainActivity = mainActivity;
        this.download = download;
    }

    @Override
    protected void onPreExecute() {
        mainActivity.getAnimeElements().clear();
        mainActivity.getAnime().getAnimes().clear();
        mainActivity.getAnimeElementAdapter().notifyDataSetChanged();
        super.onPreExecute();
    }

    @SuppressLint("Assert")
    @Override
    protected String doInBackground(String... strings) {
        assert strings.length > 1;
        String nickname = strings[0];
        String destin = MessageFormat.format("{0}/users/{1}.xml", mainActivity.getFilesDir(), nickname);
        if (download) {
            try {
                URL url = new URL(MessageFormat.format("https://shikimori.one/{0}/list_export/animes.xml", nickname));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                if (!new File(destin).exists()) new File(destin).getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(destin));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + '\n');
                    writer.flush();
                }
                reader.close();
                writer.close();
            } catch (MalformedURLException ignored11) {
                ignored11.fillInStackTrace();
            } catch (IOException ignored12) {
                ignored12.fillInStackTrace();
            }
        }
        return destin;
    }

    @Override
    protected void onPostExecute(String xml) {
        super.onPostExecute(xml);
        assert xml != null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(xml)));
            String line;
            while ((line = reader.readLine()) != null) {
                parseXML(line);
            }
            reader.close();
            mainActivity.getAnime().filter();
            mainActivity.getAnimeElementAdapter().notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();//TODO Add notification
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML(String line) {
        if (line.matches("( )*<anime>( )*")) {
            currentAnime = new AnimeElement();
        } else if (line.matches("( )*</anime>( )*")) {
            mainActivity.getAnimeElements().add(currentAnime);
            mainActivity.getAnime().getAnimes().add(currentAnime);
        } else if (line.matches("( )*<series_title>.*</series_title>( )*")) {
            currentAnime.setTitle(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')));
        } else if (line.matches("( )*<series_type>.*</series_type>( )*")) {
            switch (line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))) {
                case "tv":
                    currentAnime.setType(TypeAnime.TV);
                    break;
                case "movie":
                    currentAnime.setType(TypeAnime.Movie);
                    break;
                case "special":
                    currentAnime.setType(TypeAnime.Special);
                    break;
                case "ova":
                    currentAnime.setType(TypeAnime.OVA);
                    break;
                case "ona":
                    currentAnime.setType(TypeAnime.ONA);
                    break;
                case "music":
                    currentAnime.setType(TypeAnime.Music);
                    break;
                default:
                    Log.e("TYPE:", line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')));
            }
        } else if (line.matches("( )*<series_episodes>.*</series_episodes>( )*")) {
            currentAnime.setEpisodesAll(Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))));
        } else if (line.matches("( )*<my_watched_episodes>.*</my_watched_episodes>( )*")) {
            currentAnime.setEpisodesSeen(Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))), currentAnime.getEpisodesAll());
        } else if (line.matches("( )*<my_times_watched>*.</my_times_watched>( )*")) {
            currentAnime.setEpisodesSeenCount(Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))));
        } else if (line.matches("( )*<my_score>.*</my_score>( )*")) {
            currentAnime.setScore(Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))));
        } else if (line.matches("( )*<shiki_status>.*</shiki_status>( )*")) {
            switch (line.substring(line.indexOf('>') + 1, line.lastIndexOf('<'))) {
                case "Completed":
                    currentAnime.setStatus(StatusAnime.Completed);
                    break;
                case "On-Hold":
                    currentAnime.setStatus(StatusAnime.OnHold);
                    break;
                case "Watching":
                    currentAnime.setStatus(StatusAnime.Watching);
                    break;
                case "Dropped":
                    currentAnime.setStatus(StatusAnime.Dropped);
                    break;
                case "Rewatching":
                    currentAnime.setStatus(StatusAnime.Rewatching);
                    break;
                case "Plan to Watch":
                    currentAnime.setStatus(StatusAnime.Planned);
                    break;
                default:
                    Log.e("STATUS:", line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')));
            }
        }
    }
}
