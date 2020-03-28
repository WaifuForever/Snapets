package com.example.snapets.view.menu.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snapets.R;
import com.example.snapets.controller.PostController;
import com.example.snapets.model.Post;
import com.example.snapets.view.ExpoList;
import com.example.snapets.view.menu.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment implements ExpoList, OnListFragmentInteractionListener {

    private FeedViewModel feedViewModel;
    private ListView postList;
    private PostAdapter adapter;

    private RecyclerView recyclerView;
    private ArrayList<Post> posts;

    public FeedFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       // feedViewModel = ViewModelProviders.of(this).get(FeedViewModel.class);


        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        recyclerView = view.findViewById(R.id.post_list);


        final TextView textView = recyclerView.findViewById(R.id.text_home);




        PostController.getInstance().showList(this);

       /* feedViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return view;
    }




    @Override
    public void showList(List list, boolean success) {
        if (list == null || !success){
            Toast.makeText(getContext(), "Houve um erro no acesso ao banco de dados!", Toast.LENGTH_LONG).show();
            return;
        } else if (list.size() == 0){
            Toast.makeText(getContext(), "Não há posts cadastrados!", Toast.LENGTH_LONG).show();
            return;
        }





        RecyclerView.Adapter adapter = new PostAdapter(this.getContext(),(List<Post>) list, this );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onListFragmentInteraction(@NonNull Post post) {

    }

}