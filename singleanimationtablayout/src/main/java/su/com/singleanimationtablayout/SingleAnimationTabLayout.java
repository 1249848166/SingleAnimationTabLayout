package su.com.singleanimationtablayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.ArgbEvaluator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleAnimationTabLayout extends CoordinatorLayout{

    enum OffsetState{
        //上下滑动的状态，分为显示图片还是颜色，防止在滑动过程中频繁设置图片
        IMAGE,COLOR
    }
    OffsetState lastState=OffsetState.IMAGE;
    OffsetState currentState=OffsetState.IMAGE;

    Context context;
    Toolbar toolbar;
    AppBarLayout appbar;
    TabLayout tabLayout;
    TextView title;
    CircleImageView icon;
    ViewPager viewPager;
    List<Fragment> fragments;
    List<String> titles;
    List<Integer> icons;
    List<String> iconUrls;
    List<String> imageUrls;
    List<Integer> imageResources;
    List<String> tabItemNames;
    List<DoubleColor> doubleColors;

    ImageLoader imageLoader;
    RequestQueue requestQueue;
    ImageLoader.ImageCache imageCache;
    HashMap<String,Bitmap> map;

    int page=0;
    float colorAppearRatio=0.5f;
    boolean canGoBack=false;
    String barTitle="";

    public SingleAnimationTabLayout(Context context) {
        this(context,null);
    }

    public SingleAnimationTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SingleAnimationTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initTools();
        initViews();
    }

    private void initTools() {
        requestQueue= Volley.newRequestQueue(context);
        imageCache=new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                if(map==null)
                    return null;
                else
                    return map.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                if(map==null)
                    map=new HashMap<>();
                map.put(url,bitmap);
            }
        };
        imageLoader=new ImageLoader(requestQueue,imageCache);
    }

    private void initViews() throws RuntimeException{
        StatusBarUtil.setTranslucentForCoordinatorLayout((Activity) context,0);
        LayoutInflater.from(context).inflate(R.layout.layout_main,this,true);
        appbar=findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);
        title =findViewById(R.id.text);
        icon=findViewById(R.id.icon);
        tabLayout = findViewById(R.id.tab);
        viewPager=findViewById(R.id.viewPager);
    }

    public void finishInitialization(){
        android.support.v7.app.ActionBar actionBar=((AppCompatActivity)context).getSupportActionBar();
        if(canGoBack&&actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(actionBar!=null)
            actionBar.setTitle(barTitle);
        @SuppressLint("RestrictedApi")
        final ArgbEvaluator evaluator=new ArgbEvaluator();
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float ratio=Math.abs((float)verticalOffset/ toolbar.getMeasuredHeight());
                title.setScaleX(ratio);
                title.setScaleY(ratio);
                title.setAlpha(ratio);
                icon.setScaleX(ratio);
                icon.setScaleY(ratio);
                icon.setAlpha(ratio);
                if(Math.abs(verticalOffset)>= toolbar.getMeasuredHeight()*colorAppearRatio){
                    currentState=OffsetState.COLOR;
                    if(doubleColors!=null&&doubleColors.size()>0) {
                        @SuppressLint("RestrictedApi")
                        int color = (int) evaluator.evaluate(ratio, doubleColors.get(page).getFromColor(), doubleColors.get(page).getToColor());
                        appbar.setBackgroundColor(color);
                        lastState=OffsetState.COLOR;
                    }
                }else{
                    currentState=OffsetState.IMAGE;
                    if(currentState!=lastState) {
                        getImage(page);
                        lastState=OffsetState.IMAGE;
                    }
                }
            }
        });
        if(imageResources!=null&&imageResources.size()>0){
            appbar.setBackgroundResource(imageResources.get(0));
        }else if(imageUrls!=null&&imageUrls.size()>0){
            getImage(0);
        }else if(doubleColors!=null&&doubleColors.size()>0){
            appbar.setBackgroundColor(doubleColors.get(0).getFromColor());
        }else{
            appbar.setBackgroundColor(getResources().getColor(R.color.default_color));
        }
        if(titles!=null&&titles.size()>0)
            title.setText(titles.get(0));
        getIconImage(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page=position;
                getImage(position);
                getIconImage(position);
                if(titles!=null&&titles.size()>0)
                    title.setText(titles.get(position%titles.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(((AppCompatActivity)context).getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position%fragments.size());
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabItemNames.get(position%tabItemNames.size());
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getIconImage(int i) {
        if(icons!=null&&icons.size()>0) {
            icon.setVisibility(VISIBLE);
            icon.setImageResource(icons.get(i%icons.size()));
        }
        else if(iconUrls!=null&&iconUrls.size()>0) {
            icon.setVisibility(VISIBLE);
            imageLoader.get(iconUrls.get(i%iconUrls.size()), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    icon.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("获取图标出错", error.toString());
                }
            });
        }
        else
            icon.setVisibility(GONE);
    }

    @SuppressLint("ResourceAsColor")
    private void getImage(int i) {
        if(imageResources!=null&&imageResources.size()>0){
            appbar.setBackgroundResource(imageResources.get(i%imageResources.size()));
        }else if(imageUrls!=null&&imageUrls.size()>0)
            imageLoader.get(imageUrls.get(i%imageUrls.size()), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    BitmapDrawable drawable=new BitmapDrawable(response.getBitmap());
                    appbar.setBackgroundDrawable(drawable);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("获取网络图片出错",error.toString());
                }
            });
        else if(doubleColors!=null&&doubleColors.size()>0)
            appbar.setBackgroundColor(doubleColors.get(i).getFromColor());
        else
            appbar.setBackgroundColor(R.color.default_color);
    }

    public void setViewPagerFragments(List<Fragment> fragments){
        this.fragments=fragments;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setImageResources(List<Integer> imageResources) {
        this.imageResources = imageResources;
    }

    public void setTabItemNames(List<String> tabItemNames) {
        this.tabItemNames = tabItemNames;
    }

    public void setDoubleColors(List<DoubleColor> doubleColors) {
        this.doubleColors = doubleColors;
    }

    public int getPage() {
        return page;
    }

    public void setColorAppearRatio(float colorAppearRatio) {
        this.colorAppearRatio = colorAppearRatio;
    }

    public void setIcons(List<Integer> icons) {
        this.icons = icons;
    }

    public void setIconUrls(List<String> iconUrls) {
        this.iconUrls = iconUrls;
    }

    public void setCanGoBack(boolean canGoBack) {
        this.canGoBack = canGoBack;
    }

    public void setBarTitle(String barTitle) {
        this.barTitle = barTitle;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public AppBarLayout getAppbar() {
        return appbar;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public TextView getTitle() {
        return title;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public HashMap<String, Bitmap> getMap() {
        return map;
    }
}
