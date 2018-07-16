# SingleAnimationTabLayout
## 1.效果图
![image](https://github.com/1249848166/SingleAnimationTabLayout/blob/master/app/src/main/gif.gif)
## 2.添加引用
在project的build.gradle中添加：<br>
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在app的build.gradle中添加：<br>
```Java
	dependencies {
	        implementation 'com.github.1249848166:SingleAnimationTabLayout:2.1.0'
	}

```
从1.0开始就发现依赖冲突，然后一晚上重新发布了几十次也不容易。不过终于可以用了。2.1.0版本可以正常使用，之前的版本都有依赖冲突，无法正常使用。
## 3.在你的项目中的使用示例
布局
```Java
<?xml version="1.0" encoding="utf-8"?>
<su.com.singleanimationtablayout.SingleAnimationTabLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:background="#aaaaaa"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
activity
```Java
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

```
## 4.注意事项
* a.activity必须extends AppCompatActivity，因为用到了里面的新特性。
* b.框架内引入了三个开源库，分别是下面这三个：
```Java
    implementation 'com.jaeger.statusbaruitl:library:1.3.5'//这个依赖用来改变状态栏渗透效果
    implementation 'eu.the4thfloor.volley:com.android.volley:2015.05.28'//这个依赖用来加载网络图片
    implementation 'de.hdodenhof:circleimageview:2.2.0'//这个依赖用来制作圆形图片
```
如上所见用到了他们的功能。如果需要用到这些功能的可以不用再添加这些依赖了。
* c.因为用到titlebar，所以在manifest中，需要修改当前activity的theme为没有actionbar的，否则会冲突。
下面在styles文件下，application的主题下新建一个主题，取名为MyNotitle
```Java
<!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
    
    <style name="MyNotitle" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="windowNoTitle">true</item>
    </style>
```
然后在manifest文件里，整个application主题用默认的，当前activity这个主题设置为MyNotitle
```Java
<application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".activity.MainActivity"
            android:theme="@style/MyNotitle"
            >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
```
* d.因为用到了网络，所以需要在manifest中添加网络权限
```Java
<uses-permission android:name="android.permission.INTERNET"/>
```
* e.如果你使用了更新的support或design版本，可以通过下面方式将本依赖中的旧版本排除，使用你项目的新版本：
```Java
    implementation ('com.github.1249848166:SingleAnimationTabLayout:2.1.0', {
        exclude group: 'com.android.support', module: 'design'
    })
    implementation 'com.android.support:appcompat-v7:x.x.x'
    implementation 'com.android.support:design:x.x.x'
```
其中x指代数字
* f.如果以上还有什么疑惑，请查阅源项目中的案例，或者加qq1249848166交流。
