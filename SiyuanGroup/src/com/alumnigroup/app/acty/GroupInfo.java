package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alumnigroup.adapter.BaseOnPageChangeListener;
import com.alumnigroup.adapter.BaseViewPagerAdapter;
import com.alumnigroup.adapter.MemberAdapter;
import com.alumnigroup.api.GroupAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.MGroup;
import com.alumnigroup.entity.MGroup.Memberships;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.imple.ResponseHandler;
import com.alumnigroup.utils.DataPool;
import com.alumnigroup.utils.JsonUtils;
import com.alumnigroup.utils.L;
import com.alumnigroup.widget.PullAndLoadListView;
import com.alumnigroup.widget.PullAndLoadListView.OnLoadMoreListener;
import com.alumnigroup.widget.PullToRefreshListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 圈子列表
 * 
 * @author Jayin Ton
 * 
 */
public class GroupInfo extends BaseActivity {
	private MGroup group;
	private View btn_back, btn_edit, btn_info, btn_member, btn_share, btn_join,
			btn_invite, btn_exitGroup, btn_more;
	private TextView tv_owner, tv_numMember, tv_description, tv_groupName;
	private ImageView iv_avatar;
	private User user;
	private ViewPager viewpager;
	private List<View> btns = new ArrayList<View>();
	private GroupAPI api;
	private PullAndLoadListView lv_member;
	private List<User> data_user;
	private MemberAdapter adapter_member;
	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_groupinfo);
		initData();
		initPopupWindow();
		initLayout();
		initController();
	}

	private void initPopupWindow() {
		View view = getLayoutInflater().inflate(R.layout.popup_acty_groupinfo,
				null);

		(view.findViewById(R.id.manage)).setOnClickListener(this);
		(view.findViewById(R.id.join)).setOnClickListener(this);
		(view.findViewById(R.id.exit)).setOnClickListener(this);
		(view.findViewById(R.id.createActivity)).setOnClickListener(this);

		mPopupWindow = new PopupWindow(view);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOutsideTouchable(true);

		// 控制popupwindow的宽度和高度自适应
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupWindow.setWidth(view.getMeasuredWidth());
		mPopupWindow.setHeight(view.getMeasuredHeight());

	}

	private void initController() {
		lv_member.setAdapter(adapter_member);
		lv_member.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				api.view(group.getId(), new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						boolean canRefresh = true;
						MGroup mGroup = MGroup.create_by_json(obj.toString());
						List<User> newData = new ArrayList<User>();
						for (Memberships ms : mGroup.getMemberships()) {
							newData.add(ms.getUser());
						}
						if (newData != null) {
							if (newData.size() == 0) {
								toast("还没有会员!");
								canRefresh = false;
							} else {
								data_user.clear();
								data_user.addAll(newData);
								adapter_member.notifyDataSetChanged();
								canRefresh = false;
							}
						}
						if (!canRefresh)
							lv_member.setCanRefresh(false, false);
						lv_member.onRefreshComplete();
						// if(canRefresh)lv_member.setCanRefresh(false, "没有更多");
						// if(canRefresh)lv_member.setCanRefresh(false, false);

					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						toast("网络异常 错误代码:" + errorCode);
						lv_member.onRefreshComplete();
					}
				});
			}
		});
		lv_member.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

			}
		});
		lv_member.setCanLoadMore(false);
		lv_member.toRefresh();
	}

	@Override
	protected void initData() {
		api = new GroupAPI();
		group = (MGroup) getSerializableExtra("group");
		DataPool dp = new DataPool(DataPool.SP_Name_User, this);
		user = (User) dp.get(DataPool.SP_Key_User);

		data_user = new ArrayList<User>();
		adapter_member = new MemberAdapter(data_user, getContext());

	}

	@Override
	protected void initLayout() {
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_info = _getView(R.id.acty_groupinfo_footer_groupInfo);
		btn_member = _getView(R.id.acty_groupinfo_footer_groupMenber);
		btn_share = _getView(R.id.acty_groupinfo_footer_groupShare);
		btn_more = _getView(R.id.acty_head_btn_more);

		btn_back.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_member.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		initViewPager();
	}

	private void initViewPager() {
		viewpager = (ViewPager) _getView(R.id.acty_group_content);
		View info = getLayoutInflater().inflate(
				R.layout.frame_acty_groupinfo_groupinfo, null);
		View member = getLayoutInflater().inflate(
				R.layout.frame_acty_groupinfo_groupmember, null);
		View share = getLayoutInflater().inflate(
				R.layout.frame_acty_groupinfo_groupshare, null);

		lv_member = (PullAndLoadListView) member
				.findViewById(R.id.frame_acty_groupinfo_groupmember_listview);

		tv_owner = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_owner);
		tv_numMember = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_numMember);
		tv_description = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_description);
		tv_groupName = (TextView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_tv_groupName);
		iv_avatar = (ImageView) info
				.findViewById(R.id.frame_acty_groupinfo_groupinfo_iv_avater);

		btns.add(btn_info);
		btns.add(btn_member);
		btns.add(btn_share);
		L.i(user.getId() + "");
		// // 不是圈子拥有者
		// if (user.getId() != group.getOwner().getId()) {
		// btn_deleteGroup.setVisibility(View.GONE);
		// btn_edit.setVisibility(View.INVISIBLE);
		// }

		ImageLoader.getInstance().displayImage(
				RestClient.BASE_URL + group.getAvatar(), iv_avatar);
		tv_owner.setText(group.getOwner().getProfile().getName());
		tv_numMember.setText(group.getNumMembers() + "");
		tv_description.setText(group.getDescription());
		tv_groupName.setText(group.getName());

		List<View> views = new ArrayList<View>();
		views.add(info);
		views.add(member);
		views.add(share);
		viewpager.setAdapter(new BaseViewPagerAdapter(views));
		viewpager.setOnPageChangeListener(new BaseOnPageChangeListener(btns));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.acty_head_btn_compose:
			toast("compose");
			break;
		case R.id.acty_groupinfo_footer_groupInfo:
			viewpager.setCurrentItem(0);
			break;
		case R.id.acty_groupinfo_footer_groupMenber:
			viewpager.setCurrentItem(1);
			break;
		case R.id.acty_groupinfo_footer_groupShare:
			viewpager.setCurrentItem(2);
			break;
		case R.id.acty_head_btn_more:
			if (!mPopupWindow.isShowing())
				mPopupWindow.showAsDropDown(btn_more);
			break;
		case R.id.manage:
			toast("manage");
			mPopupWindow.dismiss();
			toManagePage();
			break;
		case R.id.join:
			mPopupWindow.dismiss();
			joinActivity();
			break;
		case R.id.exit:
			mPopupWindow.dismiss();
			exitGroup();
			// editGroup();
			break;
		case R.id.createActivity:
			mPopupWindow.dismiss();
			Intent intent = new Intent(this, ActivitiesPublish.class);
			intent.putExtra("group", group);
			openActivity(intent);
			break;
		default:
			break;
		}
	}

	private void toManagePage() {
		Intent intent = new Intent(this, GroupManage.class);
		intent.putExtra("group", group);
		openActivity(intent);
	}

	private void joinActivity() {
		api.join(group.getId(), new ResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] data) {
				String json = new String(data);
				if (JsonUtils.isOK(json)) {
					toast("加入成功");
				} else {
					toast("Error:" + JsonUtils.getErrorString(json));
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] header, byte[] data,
					Throwable err) {
				toast("网络异常 错误代码:" + statusCode);
			}
		});
	}

	private void exitGroup() {
		api.exit(group.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("已退出该群");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("退出失败 错误码:" + errorCode);

			}
		});

	}

}
