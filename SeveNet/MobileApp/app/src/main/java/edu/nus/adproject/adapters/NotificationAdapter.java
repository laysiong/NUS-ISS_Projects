package edu.nus.adproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.models.Notification;
import edu.nus.adproject.models.NotificationStatus;
import edu.nus.adproject.services.NotificationService;

public class NotificationAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private List<Notification> notifications;
    private int currentUserId;
    private NotificationService notificationService;

    public NotificationAdapter(@NonNull Context context, @NonNull List<Notification> notifications, int currentUserId) {
        super(context, 0, notifications);
        this.context = context;
        this.notifications = notifications;
        this.notificationService = new NotificationService();
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @Nullable ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);
        }

        Notification notification = notifications.get(pos);

        TextView mNotiTitleTxt = convertView.findViewById(R.id.notiTitleTxt);
        TextView mNotiContextTxt = convertView.findViewById(R.id.notiContextTxt);
        TextView mNotiTimestampTxt = convertView.findViewById(R.id.notiTimestampTxt);

        mNotiTitleTxt.setText(notification.getTitle());
        mNotiContextTxt.setText(notification.getMessage());
        mNotiTimestampTxt.setText(notification.getNotificationTime());

        //change color if read
        if (notification.getNotificationStatus().equals(NotificationStatus.Read)) {

        }
        else {

        }


        return convertView;
    }
}
