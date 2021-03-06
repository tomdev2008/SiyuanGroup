package com.alumnigroup.app.acty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alumnigroup.api.BusinessAPI;
import com.alumnigroup.api.RestClient;
import com.alumnigroup.api.StarAPI;
import com.alumnigroup.app.AppInfo;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.R;
import com.alumnigroup.entity.Cocomment;
import com.alumnigroup.entity.Cooperation;
import com.alumnigroup.entity.ErrorCode;
import com.alumnigroup.entity.User;
import com.alumnigroup.imple.JsonResponseHandler;
import com.alumnigroup.utils.CalendarUtils;
import com.alumnigroup.utils.CommonUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.widget.CommentView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BusinessDetail extends BaseActivity {
	private Cooperation c;
	private View btn_back, btn_edit, btn_end, btn_space, btn_comment,
			btn_favourite;
	private View owner, common;
	private TextView tv_username, tv_projectname, tv_deadline, tv_description,
			tv_notify,tv_createtime;
	private ImageView iv_avatar;
	private User user;
	private CommentView commentView;
	private BusinessAPI api;
	private List<Cocomment> data;
	private CocommentAdapter adatper;
	private BroadcastReceiver mReceiver;
	private ImageView iv_pic1, iv_pic2, iv_pic3;
	private int isFavourite = 0;
	private TextView tv_favourite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_businessdetail);
		initData();
		initLayout();
		openReceiver();
	}
	private void openReceiver() {
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals( // 评论成功后添加评论条目
						Constants.Action_Bussiness_Comment_Ok)) {
					if (data.isEmpty())
						tv_notify.setVisibility(View.GONE);
					Cocomment cocomment = (Cocomment) intent
							.getSerializableExtra("cocomment");
					CommonUtils.reverse(data);
					data.add(cocomment);
					CommonUtils.reverse(data);
					commentView.setAdapter(new CocommentAdapter(data));
				}else if(intent.getAction().equals(Constants.Action_Bussiness_Edit)){//修改资料
				   c =(Cooperation) intent.getSerializableExtra("cooperation");
				   fillInData();
				}

			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Action_Bussiness_Comment_Ok);
		filter.addAction(Constants.Action_Bussiness_Edit);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null)
			unregisterReceiver(mReceiver);
	}

	@Override
	protected void initData() {
		isFavourite  =getIntent().getIntExtra("isFavourite",0);
		c = (Cooperation) getSerializableExtra("cooperation");
		user = AppInfo.getUser(getContext());
		api = new BusinessAPI();
		data = new ArrayList<Cocomment>();
		adatper = new CocommentAdapter(data);
	}

	@Override
	protected void initLayout() {
		owner = _getView(R.id.linly_owner);
		common = _getView(R.id.linly_common);

		tv_username = (TextView) _getView(R.id.tv_username);
		tv_projectname = (TextView) _getView(R.id.tv_projectname);
		tv_deadline = (TextView) _getView(R.id.tv_deadline);
		tv_description = (TextView) _getView(R.id.tv_description);
		tv_notify = (TextView) _getView(R.id.tv_notify);
		iv_avatar = (ImageView) _getView(R.id.iv_avatar);
		tv_createtime =  (TextView) _getView(R.id.tv_createtime);
		tv_favourite = (TextView)_getView(R.id.tv_favourite);

		iv_pic1 = (ImageView) _getView(R.id.iv_pic1);
		iv_pic2 = (ImageView) _getView(R.id.iv_pic2);
		iv_pic3 = (ImageView) _getView(R.id.iv_pic3);

		for (int i = 0; i < c.getPictures().size(); i++) {
			switch (i) {
			case 0:
				iv_pic1.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + c.getPictures().get(i).getPath(),
						iv_pic1);
				iv_pic1.setOnClickListener(this);

				break;
			case 1:
				iv_pic2.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + c.getPictures().get(i).getPath(),
						iv_pic2);
				iv_pic2.setOnClickListener(this);
				break;
			case 2:
				iv_pic3.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL + c.getPictures().get(i).getPath(),
						iv_pic3);
				iv_pic3.setOnClickListener(this);
				break;
			default:
				break;
			}
		}
		btn_back = _getView(R.id.acty_head_btn_back);
		btn_edit = _getView(R.id.btn_edit);
		btn_end = _getView(R.id.btn_end);
		btn_space = _getView(R.id.btn_space);
		btn_comment = _getView(R.id.btn_comment);
		btn_favourite = _getView(R.id.btn_favourite);
		
		btn_back.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_end.setOnClickListener(this);
		btn_space.setOnClickListener(this);
		btn_comment.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);

		commentView = (CommentView) _getView(R.id.commentlist);
		fillInData();
		
		// api get comment list
		api.getCommentList(c.getId(), new JsonResponseHandler() {

			@Override
			public void onStart() {
				tv_notify.setVisibility(View.VISIBLE);
				tv_notify.setText("加载中...");
			}

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				List<Cocomment> newData = null;
				newData = Cocomment.create_by_jsonarray(obj.toString());
				if (newData == null) {
					toast("网络异常 解析错误");
					tv_notify.setVisibility(View.VISIBLE);
					tv_notify.setText("加载失败");
				} else if (newData.size() == 0) {
					tv_notify.setVisibility(View.VISIBLE);
					tv_notify.setText("还没有人询问");
				} else {
					data.addAll(newData);
					commentView.setAdapter(new CocommentAdapter(data));
					tv_notify.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast(ErrorCode.errorList.get(errorCode));
			}
		});
		// 用户和发布者有不同的显示
		if (c.getOwnerid() != user.getId()) {
			common.setVisibility(View.VISIBLE);
			owner.setVisibility(View.GONE);
		} else {
			owner.setVisibility(View.VISIBLE);
			common.setVisibility(View.GONE);
		}
	}

	private void fillInData() {
		if(c.getStatus().getId() ==2){ //合作已经结束了
			((TextView)btn_end.findViewById(R.id.tv_end)).setText("已结束");
			btn_end.setClickable(false);
		}
		tv_username.setText(c.getUser().getProfile().getName());
		tv_createtime.setText(CalendarUtils.getTimeFromat(c.getCreatetime(),
					CalendarUtils.TYPE_TWO));
		tv_projectname.setText(c.getName());
		tv_deadline.setText(CalendarUtils.getTimeFromat(c.getRegdeadline(),
				CalendarUtils.TYPE_TWO));
		tv_description.setText(c.getDescription());
		if (c.getUser().getAvatar() != null) {
			ImageLoader.getInstance().displayImage(
					RestClient.BASE_URL + c.getUser().getAvatar(), iv_avatar);
		} else {
			ImageLoader.getInstance().displayImage(
					"drawable://" + R.drawable.ic_image_load_normal, iv_avatar);
		}
		
		if(isFavourite==0){
			tv_favourite.setText("关注");
		}else{
			tv_favourite.setText("取消关注");
		}
	}


	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.acty_head_btn_back:
			closeActivity();
			break;
		case R.id.btn_edit:
			edit();
			break;
		case R.id.btn_end:
			end();
			break;
		case R.id.btn_space:
			// toast("to space");
			User u = c.getUser();
			intent = new Intent();
			if (user.getId() != u.getId()) {
				intent.setClass(getContext(), SpaceOther.class);
			} else {
				intent.setClass(getContext(), SpacePersonal.class);
			}
			intent.putExtra("user", c.getUser());
			openActivity(intent);
			break;
		case R.id.btn_comment:
			comment();
			break;
		case R.id.btn_favourite:
			if(tv_favourite.getText().toString().equals("关注")){
				favourite();
			}else{
				unFavourite();
			}
			break;
		case R.id.iv_pic1:
			intent = new Intent(getContext(), ImageDisplay.class);
			intent.putExtra("url", RestClient.BASE_URL
					+ c.getPictures().get(0).getPath());
			openActivity(intent);
			break;
		case R.id.iv_pic2:
			intent = new Intent(getContext(), ImageDisplay.class);
			intent.putExtra("url", RestClient.BASE_URL
					+ c.getPictures().get(1).getPath());
			openActivity(intent);
			break;
		case R.id.iv_pic3:
			intent = new Intent(getContext(), ImageDisplay.class);
			intent.putExtra("url", RestClient.BASE_URL
					+ c.getPictures().get(2).getPath());
			openActivity(intent);
			break;
		default:
			break;
		}
	}

	private void edit() {
		Intent intent = new Intent(this, BusinessPublish.class);
		intent.putExtra("cooperation", c);
		openActivity(intent);
	}

	private void comment() {
		Intent intent = new Intent(this, BusinessComment.class);
		intent.putExtra("cooperation", c);
		openActivity(intent);
	}

	private void end() {
		api.end(c.getId(), new JsonResponseHandler() {

			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("项目已结束");
			}

			@Override
			public void onFaild(int errorType, int errorCode) {
				toast("结束项目失败 " + ErrorCode.errorList.get(errorCode));
			}
		});
        closeActivity();
	}

	// 收藏的remark默认为期类型名
	private void favourite() {
		StarAPI starapi = new StarAPI();
		starapi.star(StarAPI.Item_type_business, c.getId(), "business",
				new JsonResponseHandler() {

					@Override
					public void onOK(Header[] headers, JSONObject obj) {
						toast("关注成功");
					}

					@Override
					public void onFaild(int errorType, int errorCode) {
						if(20506 == errorCode){ //已收藏
							toast("已关注");
						}else{
							toast("关注失败 " + ErrorCode.errorList.get(errorCode));
						}
					}
				});
	}
	
	private  void unFavourite(){
		StarAPI starapi = new StarAPI();
		starapi.unStar(StarAPI.Item_type_business, c.getId(), new JsonResponseHandler() {
			
			@Override
			public void onOK(Header[] headers, JSONObject obj) {
				toast("取消关注成功");
				closeActivity();
				//send broadcast
				Intent intent = new Intent(Constants.Action_Bussiness_unfavourite);
				sendBroadcast(intent);
			}
			
			@Override
			public void onFaild(int errorType, int errorCode) {
				if(20506 == errorCode){ //已收藏
					toast("已取消关注");
				}else{
					toast("取消关注失败 " + ErrorCode.errorList.get(errorCode));
				}
			}
		});
	}

	class CocommentAdapter extends BaseAdapter {

		private List<Cocomment> data;

		public CocommentAdapter(List<Cocomment> data) {
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
			ViewHolder h = null;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.item_lv_acty_communicationdetail, null);
				h.name = (TextView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_name);
				h.positime = (TextView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_posttime);
				h.body = (TextView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_body);
				h.avatar = (ImageView) convertView
						.findViewById(R.id.item_lv_acty_communicationdetai_avater);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			final Cocomment comment = data.get(position);
			h.name.setText(comment.getUser().getProfile().getName());
			h.positime.setText(CalendarUtils.getTimeFromat(comment
					.getPosttime(), CalendarUtils.TYPE_timeline));
			h.body.setText(comment.getBody());
			if (comment.getUser().getAvatar() != null) {
				ImageLoader.getInstance().displayImage(
						RestClient.BASE_URL
								+ comment.getUser().getAvatar(),
						h.avatar);
			} else {
				ImageLoader.getInstance().displayImage(
						"drawable://" + R.drawable.ic_image_load_normal,
						h.avatar);
			}
			
			h.avatar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 Intent intent = null;
					 if(comment.getUser().getId() == AppInfo.getUser(getContext()).getId()){
						  intent = new Intent(getContext(),SpacePersonal.class);
					 }else{
						  intent = new Intent(getContext(),SpaceOther.class);
					 }
					 intent.putExtra("user", comment.getUser());
					 openActivity(intent);
				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView name, positime, body;
			ImageView avatar;
		}
	}

}
