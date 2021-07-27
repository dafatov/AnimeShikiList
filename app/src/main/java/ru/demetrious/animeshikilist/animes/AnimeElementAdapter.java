package ru.demetrious.animeshikilist.animes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import ru.demetrious.animeshikilist.R;
import ru.demetrious.animeshikilist.activities.MainActivity;

public class AnimeElementAdapter extends ArrayAdapter<AnimeElement> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<AnimeElement> animeElements;
    private MainActivity mainActivity;

    public AnimeElementAdapter(MainActivity mainActivity, int resource, ArrayList<AnimeElement> animeElements) {
        super(mainActivity, resource, animeElements);
        this.mainActivity = mainActivity;
        this.animeElements = animeElements;
        this.layout = resource;
        this.inflater = LayoutInflater.from(mainActivity);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        final AnimeElement animeElement = animeElements.get(position);

        viewHolder.title.setText(animeElement.getTitle());
        viewHolder.type.setText(animeElement.getType() != null ? animeElement.getType().toString() : "");
        viewHolder.episodes.setText(MessageFormat.format("{0}{1}{2}", animeElement.getEpisodesSeen(), mainActivity.getString(R.string.anime_separator), animeElement.getEpisodesAll()));
        viewHolder.p.setMax(animeElement.getEpisodesAll());
        viewHolder.p.setProgress(animeElement.getEpisodesSeen());
        setScore(viewHolder, animeElement.getScore());
        viewHolder.seens.setText(String.valueOf(animeElement.getEpisodesSeenCount()));
        viewHolder.status.setText(animeElement.getStatus() != null ? animeElement.getStatus().toString() : "");
        if (mainActivity.getAnime().getRolledPosition() == position)
            viewHolder.title.setTextColor(Color.RED);
        else {
            viewHolder.title.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    private void setScore(ViewHolder viewHolder, int score) {
        if (score >= 9) {
            viewHolder.star0.setVisibility(View.VISIBLE);
        } else {
            viewHolder.star0.setVisibility(View.INVISIBLE);
        }
        if (score >= 7) {
            viewHolder.star1.setVisibility(View.VISIBLE);
        } else {
            viewHolder.star1.setVisibility(View.INVISIBLE);
        }
        if (score >= 5) {
            viewHolder.star2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.star2.setVisibility(View.INVISIBLE);
        }
        if (score >= 3) {
            viewHolder.star3.setVisibility(View.VISIBLE);
        } else {
            viewHolder.star3.setVisibility(View.INVISIBLE);
        }
        if (score == 0) {
            viewHolder.star4.setVisibility(View.INVISIBLE);
        } else {
            if (score % 2 == 1)
                viewHolder.star4.setImageResource(R.drawable.star_half);
            else
                viewHolder.star4.setImageResource(R.drawable.star_full);
            viewHolder.star4.setVisibility(View.VISIBLE);
        }
    }

    private class ViewHolder {
        final TextView title, type, episodes, seens, status;
        final ImageView star0, star1, star2, star3, star4;
        final ProgressBar p;

        ViewHolder(View view) {
            title = view.findViewById(R.id.anime_title);
            type = view.findViewById(R.id.anime_type);
            episodes = view.findViewById(R.id.anime_episodes_w_a);
            seens = view.findViewById(R.id.anime_seen_count);
            status = view.findViewById(R.id.anime_status);

            star0 = view.findViewById(R.id.anime_star_0);
            star1 = view.findViewById(R.id.anime_star_1);
            star2 = view.findViewById(R.id.anime_star_2);
            star3 = view.findViewById(R.id.anime_star_3);
            star4 = view.findViewById(R.id.anime_star_4);

            p = view.findViewById(R.id.anime_progress);
        }
    }
}