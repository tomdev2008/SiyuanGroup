package com.alumnigroup.api;

import java.util.List;

import com.alumnigroup.entity.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 圈子系统API
 * 
 * @author Jayin Ton
 * 
 */
public class GroupAPI {

	public GroupAPI() {

	}
	/**
	 * 接受加入申请（管理员或圈主可以操作
	 * @param membershipid 申请id
	 * @param responseHandler
	 */
	public void accept(int membershipid ,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("membershipid", membershipid+"");
		RestClient.post("/api/groups/members/accept", params, responseHandler);
	}
	/**
	 * 拒绝加入申请
	 * 管理员或圈主可以操作
	 * @param membershipid 申请id
	 * @param responseHandler
	 */
	public void reject(int membershipid ,AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		params.add("membershipid", membershipid+"");
		RestClient.post("/api/groups/members/reject", params, responseHandler);
	}

	/**
	 * 创建圈子
	 * 
	 * @param name
	 *            圈子名
	 * @param description
	 *            描述
	 * @param responseHandler
	 *            处理器
	 */
	public void createGroup(String name, String description,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("name", name);
		params.add("description", description);
		RestClient.post("/api/groups/create", params, responseHandler);
	}

	/**
	 * 圈子列表<br>
	 * NOTE：不传入任何参数时，返回圈子列表,使用getGroupList()更好 ownerid,id<0 or name=null
	 * 时不不提交改参数
	 * 
	 * @param id
	 *            活动id optional id<0时不不提交改参数
	 * @param ownerid
	 *            创建者id optional ownerid<0时不提交改参数
	 * @param name
	 *            圈子名 optional
	 * @param page
	 *            页码
	 * @param responseHandler
	 *            处理器
	 */
	public void find(int id, int ownerid, String name, int page,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (id > 0)
			params.add("id", id + "");
		if (ownerid > 0)
			params.add("ownerid", ownerid + "");
		if (name != null)
			params.add("name", name);
		if (page > 0)
			params.add("page", page + "");
		RestClient.get("/api/groups/list", params, responseHandler);
	}

	/**
	 * 获得圈子列表
	 * 
	 * @param page
	 *            页码
	 * @param responseHandler
	 *            处理器
	 */
	public void getGroupList(int page, AsyncHttpResponseHandler responseHandler) {
		find(0, 0, null, page, responseHandler);
	}

	/**
	 * 加入圈子
	 * 
	 * @param groupid
	 *            圈子id
	 * @param responseHandler
	 *            处理器
	 */
	public void join(int groupid, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("groupid", groupid + "");
		RestClient.post("/api/groups/join", params, responseHandler);
	}

	/**
	 * 获得我的圈子列表
	 * 
	 * @param page
	 *            页码
	 * @param responseHandler
	 *            处理器
	 */
	public void getMyGroupList(int page,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("page", page + "");
		RestClient.get("/api/groups/my", params, responseHandler);
	}

	/**
	 * 退出圈子
	 * 
	 * @param groupid
	 *            圈子id
	 * @param responseHandler
	 *            处理器
	 */
	public void exit(int groupid, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("groupid", groupid + "");
		RestClient.post("/api/groups/quit", params, responseHandler);
	}

	/**
	 * 查看圈子详细资料
	 * 
	 * @param id
	 *            圈子id
	 * @param responseHandler
	 *            处理器
	 *  
	 */
	public void view(int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("id", id + "");
		RestClient.get("/api/groups/view", params, responseHandler);
	}

	/**
	 * 更新圈子信息
	 * 
	 * @param groupid
	 *            圈子id
	 * @param name
	 *            圈子名字
	 * @param description
	 *            圈子描述
	 * @param responseHandler
	 *            处理器
	 */
	public void updateInfo(int groupid, String name, String description,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("groupid", groupid + "");
		params.add("name", name);
		params.add("description", description);
		RestClient.post("/api/groups/update", params, responseHandler);
	}

	/**
	 * 更新圈子头像
	 * <p>
	 * avatar :file
	 * </p>
	 * 
	 * @param groupid
	 *            圈子id
	 * @param params
	 *            包含文件的 RequestParams
	 * @param responseHandler
	 *            处理器
	 */
	public void updateAvatar(int groupid, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		params.put("groupid", groupid + "");
		RestClient.post("/api/groups/avatar/update", params, responseHandler);
	}

	/**
	 * 踢人出圈子
	 * 
	 * @param userid
	 *            被踢人的id
	 * @param groupid
	 *            圈子id
	 * @param responseHandler
	 *            处理器
	 */
	public void remove(int userid, int groupid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("userid", userid + "");
		params.add("groupid", groupid + "");
		RestClient.post("/api/groups/remove", params, responseHandler);
	}

	/**
	 * 拉好友进圈子
	 * 
	 * @param users
	 *            用户 为了获得一个或多个用户id
	 * @param groupid
	 *            圈子id
	 * @param responseHandler
	 *            处理器
	 */
	public void invite(List<User> users, int groupid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		for (int i = 0; i < users.size(); i++) {
			params.add("userid", users.get(i).getId() + "");
		}
		params.add("groupid", groupid + "");
		RestClient.post("/api/groups/pull", params, responseHandler);
	}

	/**
	 * 获得圈子的名单(包含未通过/已通过)
	 * 分页
	 * @param page
	 *           页码
	 * @param groupid
	 *            圈子id
	 * @param responseHandler
	 */
	public void getAllMembers(int page, int groupid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if (page > 0)
			params.add("page", page + "");
		if (groupid > 0)
			params.add("id", groupid + "");
		RestClient.get("/api/groups/members", params, responseHandler);
	}

	/**
	 * 获得圈子的名单(已通过)
	 * 分页
	 * @param page 页码
	 * @param groupid
	 * @param responseHandler
	 */
	public void getMembersAccepted(int page, int groupid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("isaccepted", "1");
		if (page > 0)
			params.add("page", page + "");
		if (groupid > 0)
			params.add("id", groupid + "");
		RestClient.get("/api/groups/members", params, responseHandler);
	}

	/**
	 * 获得圈子的名单(未通过)
	 * 分页
	 * @param page
	 * @param groupid
	 * @param responseHandler
	 */
	public void getMembersUnAccepted(int page, int groupid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.add("isaccepted", "0");
		if (page > 0)
			params.add("page", page + "");
		if (groupid > 0)
			params.add("id", groupid + "");
		RestClient.get("/api/groups/members", params, responseHandler);
	}

}
