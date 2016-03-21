package com.example.jd.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.jd.R;
import com.example.jd.base.BaseFragment;
import com.example.jd.conf.Constants;
import com.example.jd.fragment.menu.BaseMenuFragment;
import com.example.jd.fragment.menu.BrandMenuFragment;
import com.example.jd.fragment.menu.HomeMenuFragment;
import com.example.jd.fragment.menu.MineMenuFragment;
import com.example.jd.fragment.menu.PayCenterFragment;
import com.example.jd.fragment.menu.SearchMenuFragment;
import com.example.jd.fragment.menu.ShoppingMenuFragment;
import com.example.jd.fragment.menu.mine.LoginFragment;
import com.example.jd.utils.SpUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MainActivity extends Activity {

	private static final String	TAG	= "MainActivity";

	@ViewInject(R.id.menu_container)
	FrameLayout					menu_container;

	@ViewInject(R.id.menu_rg)
	RadioGroup					mRGroup;

	@ViewInject(R.id.menu_home)
	RadioButton					mRbHome;

	@ViewInject(R.id.menu_mine)
	RadioButton					mRbMine;

	@ViewInject(R.id.menu_brand)
	RadioButton					mRbBrand;

	@ViewInject(R.id.menu_shopping)
	RadioButton					mRbShopping;

	@ViewInject(R.id.menu_search)
	RadioButton					mRbSearch;

	private int					mCurSelctedIndex;

	private FragmentManager		mFragmentManager;
	
	public SpUtils mSpUtils	;
	
	public static boolean mIsOnlyChangeUi=false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		mFragmentManager = getFragmentManager();
		mSpUtils = new SpUtils(this);
		initData();
		initListener();
	}

	public void initData() {
		
		// 默认选中首页
		mRGroup.check(R.id.menu_home);
		FragmentTransaction tr = mFragmentManager.beginTransaction();
		tr.add(R.id.menu_container, new HomeMenuFragment());
		tr.commit();
	}

	public void initListener() {

		// 监听RidioGroup选中效果
		mRGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(mIsOnlyChangeUi){  //仅仅是改变底部MENU的UI
					mIsOnlyChangeUi = false;  //还原状态值
					return;
				}
				switch (checkedId) {
				case R.id.menu_home:// 首页
					mCurSelctedIndex = 0;
					/*FragmentTransaction tr = mFragmentManager.beginTransaction();
					tr.replace(R.id.menu_container, new HomeMenuFragment());
					tr.commit();*/
					mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					FragmentTransaction tr = mFragmentManager.beginTransaction();
					tr.add(R.id.menu_container, new HomeMenuFragment());
					tr.commit();
					
					break;
				case R.id.menu_search:// 搜索
					mCurSelctedIndex = 1;
					mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					FragmentTransaction tr1 = mFragmentManager.beginTransaction();
					tr1.replace(R.id.menu_container, new SearchMenuFragment());
					tr1.addToBackStack(null);
					tr1.commit();
					break;
				case R.id.menu_brand:// 品牌
					mCurSelctedIndex = 2;
					mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					FragmentTransaction tr2 = mFragmentManager.beginTransaction();
					tr2.replace(R.id.menu_container, new BrandMenuFragment());
					tr2.addToBackStack(null);
					tr2.commit();
					break;
				case R.id.menu_shopping:// 购物车
					mCurSelctedIndex = 3;
					mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					FragmentTransaction tr3 = mFragmentManager.beginTransaction();
					tr3.replace(R.id.menu_container, new ShoppingMenuFragment());
					//tr3.replace(R.id.menu_container, new PayCenterFragment());
					tr3.addToBackStack(null);
					tr3.commit();
					break;
				case R.id.menu_mine:// 我的
					mCurSelctedIndex = 4;
					String userId = mSpUtils.getString(Constants.USER_ID, "");
					if(TextUtils.isEmpty(userId)){
						FragmentTransaction tr4 = mFragmentManager.beginTransaction();
						tr4.replace(R.id.menu_container, new LoginFragment());
						tr4.addToBackStack(null);
						tr4.commit();
						
					}else{
						mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
						FragmentTransaction tr4 = mFragmentManager.beginTransaction();
						tr4.replace(R.id.menu_container, new MineMenuFragment());
						tr4.addToBackStack(null);
						tr4.commit();
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	/***
	 * 
	* @创建者:  陈小林
	* @描述: 该方法仅用来改变底部menu选择UI   
	*2016-3-19 下午3:27:47
	 */
	public void setChooseMenu(int position){
		mIsOnlyChangeUi = true;
		switch (position) {
		case 0:
			mRGroup.check(R.id.menu_home);
			break;
		case 1:
			mRGroup.check(R.id.menu_search);
			break;
		case 2:
			mRGroup.check(R.id.menu_brand);
			break;
		case 3:
			mRGroup.check(R.id.menu_shopping);
			break;
		case 4:
			mRGroup.check(R.id.menu_mine);
			break;

		default:
			break;
		}
	}
	
	
	
	
	public void changeToAnotherMenu(int position){
		return;
		/*switch (position) {
		case 0:
			mRGroup.check(R.id.menu_home);
			break;
		case 1:
			mRGroup.check(R.id.menu_search);
			break;
		case 2:
			mRGroup.check(R.id.menu_brand);
			break;
		case 3:
			mRGroup.check(R.id.menu_shopping);
			break;
		case 4:
			mRGroup.check(R.id.menu_mine);
			break;

		default:
			break;
		}*/
	}
}
