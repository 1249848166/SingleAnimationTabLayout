package su.com.learn.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import su.com.learn.R;
import su.com.learn.fragment.FragmentOne;
import su.com.learn.fragment.FragmentThree;
import su.com.learn.fragment.FragmentTwo;
import su.com.singleanimationtablayout.DoubleColor;
import su.com.singleanimationtablayout.SingleAnimationTabLayout;

public class MainActivity extends AppCompatActivity {

    SingleAnimationTabLayout singleAnimationTabLayout;
    List<Integer> imageResources;
    List<String> imageUrls;
    List<String> tabItemNames;
    List<String> titles;
    List<Integer> icons;
    List<String> iconUrls;
    List<Fragment> fragments;
    List<DoubleColor> doubleColors;

    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleAnimationTabLayout= (SingleAnimationTabLayout) LayoutInflater.from(this).inflate(R.layout.activity_main,null,false);
        setContentView(singleAnimationTabLayout);

        imageResources=new ArrayList<>();
        imageUrls=new ArrayList<>();
        tabItemNames=new ArrayList<>();
        titles=new ArrayList<>();
        icons=new ArrayList<>();
        iconUrls=new ArrayList<>();
        fragments=new ArrayList<>();
        doubleColors=new ArrayList<>();

        //本地图片数据（如果没有，将会加载网络图片）
//        imageResources.add(R.drawable.img1);
//        imageResources.add(R.drawable.img2);
//        imageResources.add(R.drawable.img3);

        //网络图片数据（如果没有，那么将使用下面的背景颜色（doubleColor的fromColor））
        imageUrls.add("http://bmob-cdn-16786.b0.upaiyun.com/2018/03/08/486f9b8740b4aa0380493614aa166019.jpg");
        imageUrls.add("http://bmob-cdn-16786.b0.upaiyun.com/2018/03/08/3767446a4091f216804d6741b541fba4.jpg");
        imageUrls.add("http://bmob-cdn-16786.b0.upaiyun.com/2018/03/08/ae97118d404900c38060f0a213ad2a75.jpg");

        //双颜色（当界面向上滑动到一定比例时，改用渐变颜色，从fromColor向toColor渐变）
        doubleColors.add(new DoubleColor(Color.parseColor("#55ffdd"),Color.GREEN));
        doubleColors.add(new DoubleColor(Color.WHITE,Color.RED));
        doubleColors.add(new DoubleColor(Color.GREEN,Color.BLUE));

        //小图标本地图片数据
        icons.add(R.drawable.icon1);
        icons.add(R.drawable.icon2);
        icons.add(R.drawable.icon3);

        //小图标网络图片数据
        iconUrls.add("http://bmob-cdn-16786.b0.upaiyun.com/2018/03/08/3f66a95b40f2d6c7802163322b426112.jpg");
        iconUrls.add("http://bmob-cdn-16786.b0.upaiyun.com/2018/03/08/3dce2b95403da488801897f2417f8179.jpg");
        iconUrls.add("http://bmob-cdn-16786.b0.upaiyun.com/2018/03/08/e07cf90940b88ce280155c67c6139d75.jpg");

        //每一页tabitem对应的标题
        tabItemNames.add("页面1");
        tabItemNames.add("页面2");
        tabItemNames.add("页面3");

        //每一页的标题，会在页面上滑时出现，不需要可以去除
        titles.add("标题1");
        titles.add("标题2");
        titles.add("标题3");

        //每一页的界面，用fragment管理比较方便
        fragments.add(new FragmentOne());
        fragments.add(new FragmentTwo());
        fragments.add(new FragmentThree());

        //设置一些属性
        singleAnimationTabLayout.setImageResources(imageResources);//设置本地图片
        singleAnimationTabLayout.setImageUrls(imageUrls);//设置网络图片
        singleAnimationTabLayout.setIcons(icons);//设置小图标本地图片
        singleAnimationTabLayout.setIconUrls(iconUrls);//设置小图标网络图片
        singleAnimationTabLayout.setTabItemNames(tabItemNames);//设置每个tab的标签
        singleAnimationTabLayout.setTitles(titles);//设置每一页标题
        singleAnimationTabLayout.setViewPagerFragments(fragments);//设置viewpager每一页的fragment
        singleAnimationTabLayout.setDoubleColors(doubleColors);//设置渐变颜色
        singleAnimationTabLayout.setColorAppearRatio(0.2f);//设置向上滑动距离显示渐变色的比例
        singleAnimationTabLayout.setCanGoBack(true);//设置是否显示左上角的回退箭头图标
        singleAnimationTabLayout.setBarTitle("");//设置actionbar标题

        singleAnimationTabLayout.finishInitialization();//结束所有初始化（必须放在设置属性的最后，否则界面没法显示数据）

        page=singleAnimationTabLayout.getPage();//通过这个方法获取内部viewpager当前的页面位置

        //以下方法可以获取内部组建，然后进行自定义设置
        AppBarLayout appbar=singleAnimationTabLayout.getAppbar();
        TabLayout tabLayout=singleAnimationTabLayout.getTabLayout();
        Toolbar toolbar=singleAnimationTabLayout.getToolbar();
        TextView titleTv=singleAnimationTabLayout.getTitle();
        ViewPager viewPager=singleAnimationTabLayout.getViewPager();
        HashMap<String,Bitmap> map=singleAnimationTabLayout.getMap();//这个是内部用来缓存图片的map，可以整个应用公用一个
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //启用回退箭头后，在这里判断点击。如果不是第一个activity就回退
        if(item.getItemId()==android.R.id.home&&!isTaskRoot())
            finish();
        return super.onOptionsItemSelected(item);
    }
}
