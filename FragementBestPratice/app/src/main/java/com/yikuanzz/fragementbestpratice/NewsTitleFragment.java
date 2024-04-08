package com.yikuanzz.fragementbestpratice;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {

    private boolean isTwoPane;

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<News> mNewsList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;

            public ViewHolder(View view){
                super(view);
                newsTitleText = (TextView) view.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter (List<News> newsList){
            mNewsList = newsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_title_item, parent, false);
           final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = mNewsList.get(holder.getAdapterPosition());
                    if (isTwoPane){
                        // 如果是双页模式就刷新 NewsContentFragment 中的内容
                        NewsContentFragment newsContentFragment = (NewsContentFragment)
                                getParentFragmentManager().findFragmentById(R.id.news_content_fragment);
//                        assert newsContentFragment != null;

                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    } else{
                        // 如果是单页模式就启动 NewsContentActivity
                        NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());
                    }
                }
            });

            return holder;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag, container, false);


        // 绑定 RecyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        NewsAdapter newsAdapter = new NewsAdapter(getNews());
        recyclerView.setAdapter(newsAdapter);

        MainActivity activity = (MainActivity) getActivity();
        String s = String.valueOf(activity.getTwoPlane());

        return view;
    }

    private List<News> getNews(){
        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= 50; i++){
            News news = new News();
            news.setTitle("This is news title" + i);
            news.setContent(getRandomLengthContent("This is news content" + i + "."));
            newsList.add(news);
        }
        return newsList;
    }

    private String getRandomLengthContent(String content){
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++){
            builder.append(content);
        }
        return builder.toString();
    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        //requireActivity() 返回的是宿主activity
        requireActivity().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull @NotNull LifecycleOwner source, @NonNull @NotNull Lifecycle.Event event) {
                if (event.getTargetState() == Lifecycle.State.CREATED){
                    MainActivity activity = (MainActivity) getActivity();
                    String msg = String.valueOf(activity);
                    Log.d("onViewCreated", "111");
                    Log.d("getNewsActivity", msg);
                    String msg_ = String.valueOf(activity.getTwoPlane());
                    Log.d("getNewsContentLayout", msg_);
                    isTwoPane = activity.getTwoPlane();
                    requireActivity().getLifecycle().removeObserver(this);  //这里是删除观察者
                }
            }
        });
    }
}
