package com.example.pulkit.demoapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvGitUser;
    ArrayAdapter<User> adapter;
    List<User> gitUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gitUsers = new ArrayList<>();
        lvGitUser = (ListView) findViewById(R.id.lvGitUser);
        adapter = new UserAdapter(this, R.layout.user_profile, gitUsers);
        lvGitUser.setAdapter(adapter);
        lvGitUser.setOnItemClickListener(this);
        new FetchUserTask().execute();

    }

    public class FetchUserTask extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.github.com").build();
            GithubUserService githubUserService = restAdapter.create(GithubUserService.class);
            return githubUserService.getUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            gitUsers.addAll(users);
            adapter.notifyDataSetChanged();

        }
    }

    public class UserAdapter extends ArrayAdapter<User> {
        LayoutInflater layoutInflater;
        public UserAdapter(Context context, int resource, List<User> objects) {
            super(context, resource, objects);
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View userItemView = convertView;
            if (userItemView == null) {
                userItemView = layoutInflater.inflate(R.layout.user_profile,parent,false);
            }
            ImageView userImage = (ImageView) userItemView.findViewById(R.id.userImage);
            TextView userName = (TextView) userItemView.findViewById(R.id.userName);
            User user = getItem(position);

            Picasso.with(getContext()).load(user.avatar_url).into(userImage);
            userName.setText(user.login);

            return userItemView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) ((ListView) parent).getAdapter().getItem(position);
        Toast.makeText(this, user.login, Toast.LENGTH_SHORT).show();
    }

}
