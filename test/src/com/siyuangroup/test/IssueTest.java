package com.siyuangroup.test;

import org.apache.http.Header;

import com.api.IssuesAPI;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.utils.JsonUtils;
import com.utils.TempLogin;

public class IssueTest extends BaseTestActivity {
  private int page=1;
	@Override
	public void test1Click() {
         post();
		 //search("好饿了啊！！！",null);
		// view(106);
	}

	private void view(int id) {
		 IssuesAPI api = new IssuesAPI();
		 api.view(id, new AsyncHttpResponseHandler(){
			 @Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			    String json = new String(arg2);
			    if(JsonUtils.isOK(json)){
			    	print(new String(arg2));
			    }
			    else{
			    	print("error:\n"+JsonUtils.getErrorString(json));
			    }
			}
		 });
	}

	private void post() {
		   IssuesAPI api = new IssuesAPI();
	       String title="我好饿啊！";
	       String body = "RT";
	       api.postIssue(title, body, new Response());
		
	}

	@Override
	public void test2Click() {
		  

		//   getIssueList();
		//update(106,"好饿了啊！！！","let's go");
		TempLogin.register();
	}
  

	private void update(int issueId, String title, String body) {
		IssuesAPI api = new IssuesAPI();
		api.updateIssue(issueId, title, body, new Response());
	    
	}

	private void getIssueList() {
		 IssuesAPI api = new IssuesAPI();
		 api.getIssueList(page++, new Response());
		
	}

	@Override
	public void test3Click() {
           //search("我好饿啊！",null);
		comment(106,"要一起去吃饭？");
	}

	private void comment(int issueid, String content) {
		 IssuesAPI api = new IssuesAPI();
		 api.commentIssue(issueid, content, new Response());
		
	}
    
	private void search(String title, String body) {
		 IssuesAPI api = new IssuesAPI();
         api.search(1, title, body,  new Response());		
	}

	@Override
	public void test4Click() {
      Login();
//		IssuesAPI api = new IssuesAPI();
//		api.deleteIssue(106, new Response());
	}

	private void Login() {
		//TempLogin.register();
		 TempLogin.login();
		
	}

	@Override
	public void onInit() {

	}

	@Override
	public String setTitle() {
		return "Issue api test";
	}

	@Override
	public String test1Title() {
		 return "发布";
	//	return "查看帖子";
	}

	@Override
	public String test2Title() {
		//return "更新";
		//return "getIssueList";
		return "register";
	}

	@Override
	public String test3Title() {
		return "评论";
		//return "search";
	}

	@Override
	public String test4Title() {
		// return "删除";
		return "Login";
	}

 

}
