package com.alumnigroup.app.acty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alumnigroup.api.RestClient;
import com.alumnigroup.app.BaseActivity;
import com.alumnigroup.app.CoreService;
import com.alumnigroup.app.R;
import com.alumnigroup.utils.AndroidUtils;
import com.alumnigroup.utils.Constants;
import com.alumnigroup.utils.DataPool;

/**
 * 主界面
 * 
 * @author Jayin Ton
 * @since 2013.12.11;
 * 
 */
public class Main extends BaseActivity implements OnClickListener {
	private WebView webview;
	private LinearLayout content;
	private LinearLayout parent_content;
	private int width = 0, height = 0;
	private DataPool dp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_main);
		dp = new DataPool(DataPool.SP_Name_User, this);
		initData();
		initLayout();
		checkVerison();
	}

	private void checkVerison() {
		Intent service = new Intent(getContext(), CoreService.class);
		service.setAction(Constants.Action_checkVersion);
		startService(service);
	}

	@Override
	protected void initData() {
		width = AndroidUtils.getScreenSize(getContext())[0];
		height = AndroidUtils.getScreenSize(getContext())[1];
	}

	@Override
	protected void initLayout() {
		adapteScreent();

		_getView(R.id.frame_main_one_myspace).setOnClickListener(this);

		_getView(R.id.frame_main_one_setting).setOnClickListener(this);

		_getView(R.id.frame_main_one_message).setOnClickListener(this);

		_getView(R.id.frame_main_one_allmember).setOnClickListener(this);

		_getView(R.id.frame_main_one_communication).setOnClickListener(this);

		_getView(R.id.frame_main_one_activities).setOnClickListener(this);

		_getView(R.id.frame_main_one_group).setOnClickListener(this);

		_getView(R.id.frame_main_one_business).setOnClickListener(this);

		_getView(R.id.frame_main_one_allactivity).setOnClickListener(this);

		initWebView();
	}

	// 初始化广告栏
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		webview = (WebView) _getView(R.id.acty_main_webview);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webSettings.setLoadsImagesAutomatically(true); // 自动加载图片
		webSettings.setBuiltInZoomControls(false);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				// toast("onLoadResource-->"+url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 自行处理点击事件！
				Intent intent = new Intent(Main.this, Browser.class);
				intent.putExtra("url", url);
				openActivity(intent);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.setVisibility(View.INVISIBLE);

			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					webview.setVisibility(View.VISIBLE);
				} else {
					webview.setVisibility(View.INVISIBLE);
				}
			}
		});
		webview.loadUrl(RestClient.BASE_URL + "/ad/index.html");
	}

	// 适配屏幕
	private void adapteScreent() {
		content = (LinearLayout) _getView(R.id.acty_main_content_one);
		parent_content = (LinearLayout) _getView(R.id.acty_main_content);
		android.view.ViewGroup.LayoutParams params = content.getLayoutParams();
		params.height = width;
		content.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frame_main_one_myspace:
			openActivity(SpacePersonal.class);
			break;
		case R.id.frame_main_one_setting:
			openActivity(Setting.class);
			break;
		case R.id.frame_main_one_message:
			openActivity(MessageCenter.class);
			break;
		case R.id.frame_main_one_allmember:
			openActivity(Allmember.class);
			break;
		case R.id.frame_main_one_communication:
			openActivity(Communication.class);
			break;
		case R.id.frame_main_one_activities:
			openActivity(Activities.class);
			break;
		case R.id.frame_main_one_group:
			openActivity(Group.class);
			break;
		case R.id.frame_main_one_business:
			openActivity(Business.class);
			break;
		case R.id.frame_main_one_allactivity:
			openActivity(Alldynamic.class);
			break;
		default:
			break;
		}
	}

}
