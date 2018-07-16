package su.com.learn.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import su.com.learn.R;
import su.com.learn.adapter.MyRecyclerAdapter;
import su.com.learn.decoration.MyRecyclerDecoration;

public class FragmentThree extends Fragment {
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null) {
            rootView=inflater.inflate(R.layout.frag3, container, false);
        }
        RecyclerView recycler=rootView.findViewById(R.id.recycler);
        List<String> datas=new ArrayList<>();
        for(int i=0;i<100;i++){
            datas.add("第三页数据"+i);
        }
        MyRecyclerAdapter adapter=new MyRecyclerAdapter(getContext(),datas);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        if(recycler.getItemDecorationCount()==0) {
            recycler.addItemDecoration(new MyRecyclerDecoration(1));
        }
        return rootView;
    }
}
