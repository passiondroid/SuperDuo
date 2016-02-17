package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.WidgetRemoteViewsService;
import barqsoft.footballscores.service.myFetchService;

/**
 * Created by Arif on 14-Feb-16.
 */
public class FootballScoreWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int appWidgetId : appWidgetIds){
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            setRemoteAdapter(context,views);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,new Intent(context, WidgetRemoteViewsService.class));
    }



}
