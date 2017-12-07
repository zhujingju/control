package com.grasp.control.view;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grasp.control.MainActivity;
import com.grasp.control.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/8/18.
 */

public class  LookPhotoFragmet extends Fragment {

    @BindView(R.id.look_photo_name)
    TextView lookPhotoName;
    @BindView(R.id.look_photo_back)
    ImageView lookPhotoBack;
    Unbinder unbinder;
    @BindView(R.id.look_photo_viewPager)
    ViewPager lookPhotoViewPager;
    private Context context;
    private int post;
    private ArrayList<String> name;
    private ArrayList<String> path;
    private boolean zt;

    private List<Fragment> fragments;// Tab页面列表

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.look_photo_fragmet, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        lookListener = MainActivity.lookListener;
        init();

        return view;
    }

    public void setData(int post, ArrayList<String> name, ArrayList<String> path,boolean zt) {
        this.post = post;
        this.name = name;
        this.path = path;
        this.zt=zt;
        if(lookPhotoViewPager!=null){
            lookPhotoViewPager.setCurrentItem(post);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick(R.id.look_photo_back)
    public void onViewClicked() {
        lookListener.lookListener();
    }

    private LookListener lookListener;


    public interface LookListener {

        void lookListener();

    }


    myPagerAdapter adapter;
    private void init() {
        // TODO Auto-generated method stub
        fragments = new ArrayList<Fragment>();

        if(path==null){
            return;
        }
        for (int i=0;i<path.size();i++) {
            ImageviewFragmet im_fragmet = new ImageviewFragmet();
            im_fragmet.setData(name.get(i),path.get(i));
            fragments.add(im_fragmet);
        }

        adapter=new myPagerAdapter(getChildFragmentManager(), fragments);
        lookPhotoViewPager.setAdapter(null);
        lookPhotoViewPager.setAdapter(adapter);
        Log.d("qqq",path.size()+"   "+post);
        lookPhotoViewPager.setCurrentItem(post);
        adapter.notifyDataSetChanged();

    }

    /**
     * 定义适配器
     */
    class myPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        private List<Fragment> fragmentsList;
        private FragmentManager fm;

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
            this.fm = fm;
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            //知道当前是第几页，但是每次滑动后可能会调用多次
            //这个方法是重点
            super.setPrimaryItem(container, position, object);
            fragmentsList.get(position);
            String s="";
            if(name.get(position).length()>4){
                s=name.get(position).substring(0,name.get(position).length()-4);
            }
            lookPhotoName.setText(s);

        }

        @Override
        public int getItemPosition(Object object) {
            //加此方法可以使viewpager可以进行刷新
            return PagerAdapter.POSITION_NONE;
        }

        //使用此方法刷新数据 每次都要NEW一个新的List，不然没有刷新效果 转至http://blog.sina.com.cn/s/blog_783ede03010173b4.html
        public void setFragments(ArrayList<Fragment> fragments) {
            if (fragments != null) {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : fragments) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();
            }
            this.fragmentsList = fragments;
            notifyDataSetChanged();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 注释自带的销毁方法防止页面被销毁
            //这个方法是重点
            // super.destroyItem(container, position, object);}
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        /**
         * //		 * 为选项卡绑定监听器
         * //
         */
        @Override
        public void onPageSelected(int i) {
            // TODO Auto-generated method stub
//            SetCurSelText(i);
        }
    }
}
