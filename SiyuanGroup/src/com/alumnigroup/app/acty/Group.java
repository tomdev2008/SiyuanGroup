package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.api.ActivityAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 圈子页面
 * 
 * @author Jayin Ton
 * 
 */
public class Group extends BaseActivity implements OnItemClickListener {
	private List<View> btns = new ArrayList<View>();
	private View btn_back, btn_all, btn_mycreate, btn_myjoin, btn_more;
	private PullAndLoadListView lv_all, lv_myjoin, lv_mycreate;
	private ViewPager viewpager;
	private List<MGroup> data_all, data_myjoin, data_mycreate;
	private GroupAdapter adapter_all, adapter_myjoin, adapter_mycreate;
	private int page_all = 1, page_myjoin = 1, page_mycreate = 1;
	private ActivityAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_group);
		initData();
		initLayout();
		initController();
	}

	private void initController() {
		lv_all.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});
		lv_all.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});

		lv_myjoin.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});
		lv_myjoin.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		lv_mycreate.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

			}
		});
		lv_mycreate.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		lv_all.setOnItemClickListener(this);
		lv_myjoin.setOnItemClickListener(this);
		lv_mycreate.setOnItemClickListener(this);
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_group_content);
		View all = getLayoutInflater().inflate(
				R.layout.frame_acty_group, null);
		View myjoin = getLayoutInflater().inflate(
				R.layout.frame_acty_group, null);
		View favourit = getLayoutInflater().inflate(
				R.layout.frame_acty_group, null);
		lv_all = (PullAndLoadListView) all
				.findViewById(R.id.frame_acty_group_listview);
		lv_myjoin = (PullAndLoadListView) myjoin
				.findViewById(R.id.frame_acty_group_listview);
		lv_mycreate = (PullAndLoadListView) favourit
				.findViewById(R.id.frame_acty_group_listview);

		adapter_all = new GroupAdapter(data_all);
		adapter_myjoin = new GroupAdapter(data_myjoin);
		adapter_mycreate = new GroupAdapter(data_mycreate);

		lv_all.setAdapter(adapter_all);
		lv_myjoin.setAdapter(adapter_myjoin);
		lv_mycreate.setAdapter(adapter_mycreate);

		List<View> views = new ArrayList<View>();
		views.add(all);
		views.add(myjoin);
		views.add(favourit);
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new BaseOnPageChangeListener(btns));
	}

	@Override
	protected void initData() {
		api = new ActivityAPI();
		data_all = new ArrayList<MGroup>();
		data_myjoin = new ArrayList<MGroup>();
		data_mycreate = new ArrayList<MGroup>();
	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_all = _getView(R.id.acty_group_footer_all);
		btn_myjoin = _getView(R.id.acty_group_footer_myjoin);
		btn_mycreate = _getView(R.id.acty_group_footer_mycreat);
		btn_more = _getView(R.id.acty_head_btn_more);

		btns.add(btn_all);
		btns.add(btn_myjoin);
		btns.add(btn_mycreate);

		btn_back.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_myjoin.setOnClickListener(this);
		btn_mycreate.setOnClickListener(this);
		btn_more.setOnClickListener(this);

		initViewPager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_more:
			toast("more");
			break;
		case R.id.acty_group_footer_all:
			viewpager.setCurrentItem(0, true);
			break;
		case R.id.acty_group_footer_myjoin:
			viewpager.setCurrentItem(1, true);
			break;
		case R.id.acty_group_footer_mycreat:
			viewpager.setCurrentItem(2, true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	class GroupAdapter extends BaseAdapter {
		private List<MGroup> data;

		public GroupAdapter(List<MGroup> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodler h;
			if (convertView == null) {
				h = new ViewHodler();
				convertView = getLayoutInflater().inflate(
						R.layout.item_lv_acty_group, null);
				h.name = (TextView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_ame);
				h.username = (TextView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_ownername);
				h.memberCount = (TextView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_memberCount);
				h.description = (TextView) convertView
						.findViewById(R.id.item_lv_acty_activities_ownername);
				h.avater = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_group_tv_description);
				convertView.setTag(h);
			} else {
				h = (ViewHodler) convertView.getTag();
			}
			MGroup group = data.get(position);
			h.name.setText(group.getName());
			h.username.setText("ownid" + group.getOwnerid());
			h.memberCount.setText(group.getMemberships().getUsers().size()
					+ "名会员");
			h.description.setText(group.getDescription());
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + group.getAvatar(), h.avater);
			return convertView;
		}

		class ViewHodler {
			TextView name, username, memberCount, description;
			ImageView avater;
		}
	}

}
