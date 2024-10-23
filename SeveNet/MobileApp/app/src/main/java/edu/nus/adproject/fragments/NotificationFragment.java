package edu.nus.adproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.activities.LoginActivity;
import edu.nus.adproject.adapters.NotificationAdapter;
import edu.nus.adproject.models.Notification;
import edu.nus.adproject.models.User;
import edu.nus.adproject.services.NotificationService;

public class NotificationFragment extends Fragment {
    private User user;
    private int userId;
    private static final String ARG_USER = "user";
    private ListView mNotiListView;

    private NotificationService notificationService;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notifications;

    private ImageView mLogoutMenu;
    SharedPreferences pref;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(User user) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
        }
        userId = user.getId();
        notificationService = new NotificationService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mNotiListView = view.findViewById(R.id.notisLV);

        mLogoutMenu = view.findViewById(R.id.logoutMenu);
        mLogoutMenu.setOnClickListener(v -> showPopupLogOutMenu(v));

        if (notifications ==null) {
            notifications = new ArrayList<>();
        }

        fetchAndDisplayNotifications();

        return view;
    }

    private void showPopupLogOutMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.logout_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> handleMenuLogout(item));
        popup.show();
    }

    private boolean handleMenuLogout(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            pref = requireContext().getSharedPreferences("user_credential", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("username");
            editor.remove("password");
            editor.commit();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void fetchAndDisplayNotifications() {
        new FetchNotificationsTask().execute(userId);
    }

    private class FetchNotificationsTask extends AsyncTask<Integer, Void, List<Notification>> {

        @Override
        protected List<Notification> doInBackground(Integer... params) {
            int userId = params[0];
            notifications = notificationService.getNoti(userId);
            return notifications; // Replace with your method to fetch posts
        }

        @Override
        protected void onPostExecute(List<Notification> newNotifications) {
            notificationAdapter = new NotificationAdapter(getContext(), notifications, userId);
            mNotiListView.setAdapter(notificationAdapter);
            notificationAdapter.notifyDataSetChanged();

        }
    }
}