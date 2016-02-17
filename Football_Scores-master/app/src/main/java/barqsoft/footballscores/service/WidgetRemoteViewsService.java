package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.text.SimpleDateFormat;
import java.util.Date;
import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by Arif on 14-Feb-16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetRemoteViewsService extends RemoteViewsService{


    public static final int COL_DATE = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_LEAGUE = 5;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_HOME_ID = 10;
    public static final int COL_AWAY_ID = 11;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                // Dates between which we will be fetching data
                Date fragmentdate = new Date(System.currentTimeMillis()+(-2 * 86400000));
                Date fragmentdatePlus = new Date(System.currentTimeMillis()+(5 * 86400000));
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

                data = getContentResolver().query(DatabaseContract.scores_table.buildScoreBetweenFiveDays(),
                        null,
                        null,
                        new String[]{ mformat.format(fragmentdate), mformat.format(fragmentdatePlus) },
                        DatabaseContract.scores_table.DATE_COL + " DESC");

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item);

                String homeName = data.getString(COL_HOME);
                String awayName = data.getString(COL_AWAY);
                int homeCrest = Utilies.getTeamCrestByTeamName(homeName);
                views.setImageViewResource(R.id.home_crest, homeCrest);
                int awayCrest = Utilies.getTeamCrestByTeamName(awayName);
                views.setImageViewResource(R.id.away_crest, awayCrest);

                String date = data.getString(COL_DATE);
                String league = Utilies.getLeague(data.getInt(COL_LEAGUE));
                String scores = Utilies.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS));

                views.setTextViewText(R.id.data_textview, date);
                views.setTextViewText(R.id.score_textview, scores);
                views.setTextViewText(R.id.league_textview, league);
                views.setTextViewText(R.id.home_name, homeName);
                views.setTextViewText(R.id.away_name, awayName);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(0);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
