package com.example.hw7;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {

    public String TAG = "demo";
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public StorageReference storageReference = firebaseStorage.getReference();

    public UsersAdapter(@NonNull Context context, int resource, @NonNull List<User> users) {
        super(context, resource, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        TextView tv_firstName = convertView.findViewById(R.id.tv_user_firstName);
        tv_firstName.setText(user.firstName);

        TextView tv_lastName = convertView.findViewById(R.id.tv_user_lastName);
        tv_lastName.setText(user.lastName);

        TextView tv_gender = convertView.findViewById(R.id.tv_user_gender);
        tv_gender.setText(user.gender);

        TextView tv_email = convertView.findViewById(R.id.tv_user_email);
        tv_email.setText(user.emailAddress);

        ImageView iv_avatar = convertView.findViewById(R.id.iv_user_avatar);

        Log.d(TAG, "getView: " + user.url);
        StorageReference sf = storageReference.child("avatars").child(user.userId + ".png");
        //sf.

        return convertView;
    }
}
